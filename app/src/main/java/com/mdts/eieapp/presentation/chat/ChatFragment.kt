package com.mdts.eieapp.presentation.chat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.mdts.eieapp.R
import com.mdts.eieapp.base.BaseFragment
import com.mdts.eieapp.base.collectWhenOwnerCreated
import com.mdts.eieapp.base.collectWhenOwnerStarted
import com.mdts.eieapp.data.model.ChatRequestDTO
import com.mdts.eieapp.data.model.MessageRequestItemDTO
import com.mdts.eieapp.databinding.FragmentChatBinding
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.presentation.chat.adapter.ListMessageUIEvent
import com.mdts.eieapp.presentation.chat.adapter.Message
import com.mdts.eieapp.presentation.chat.adapter.MessageAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Locale

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    // Speech to Text
    private lateinit var speechRecognizer: SpeechRecognizer
    // Text to Speech
    private lateinit var textToSpeech: TextToSpeech

    // Adapter
    private lateinit var chatAdapter: MessageAdapter
    private val messages: ArrayList<Message> = ArrayList()

    override val viewModel by viewModels<ChatViewModel>()
    override fun initBindingObject(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // user message
        var isUserTyping = false
        binding.messageEditText.addTextChangedListener { editable ->
            isUserTyping = editable.toString().isNotEmpty()
            if (isUserTyping) {
                binding.micButton.setImageResource(R.drawable.ic_send_24)
            } else {
                binding.micButton.setImageResource(R.drawable.ic_mic_24)
            }
        }
        binding.micButton.setOnClickListener {
            if (isUserTyping) {
                // send user message
                val userContent = binding.messageEditText.text.toString()
                if (userContent.isNotEmpty()) {
                    addMessageToChatRecyclerView(Message("user", userContent))
                    sendChatToOpenAIAndRetrieveResponse()
                    binding.messageEditText.text?.clear()
                }
            } else {
                // check record audio permission
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    startListening()
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_CODE)
                }
            }
        }

        /* Speech to Text */
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                binding.micButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_64_red)
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(p0: Float) {
                // resize micButton given rms value
                val scale = p0 / 2
                if (scale < 1 || scale > 4) return
                binding.micButton.scaleX = scale
                binding.micButton.scaleY = scale

            }

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {
                binding.micButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_64_blue)
                // restore micButton size
                binding.micButton.scaleX = 1f
                binding.micButton.scaleY = 1f
            }

            override fun onError(p0: Int) {}

            override fun onResults(p0: Bundle?) {
                val result = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null){
                    result.forEach { item -> Timber.d(">>>>${item}") }
                    binding.messageEditText.setText(result[0])
                }
            }

            override fun onPartialResults(p0: Bundle?) {}
            override fun onEvent(p0: Int, p1: Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(recognitionListener)

        /* Text to Speech */
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(requireContext(), "Text to speech language not supported", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Text to speech initialization failed", Toast.LENGTH_LONG).show()
            }
        }
        // chat recycler view
        chatAdapter = MessageAdapter(messages)
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
        }

        chatAdapter.onClicked = {
            when (it) {
                is ListMessageUIEvent.OnItemClicked -> {
                    //Show bottom sheet
                    Timber.d(">>>>Show bottom sheet")
                    /*val options = NavOptions.Builder()
                        .setPopUpTo(viewModel.parentId, false)
                        .build()
                    val bundle = Bundle().apply {
                        putSerializable(
                            DocumentFragment.DOCUMENT_ITEM_BOTTOM_SHEET,
                            viewModel.getDocument()
                        )
                    }*/
                    findNavController().navigate(
                        R.id.vocabularyBottomSheetDialogFragment
                    )
                }
            }
        }
        // clear button
        binding.clearButton.setOnClickListener {
            messages.clear()
            binding.chatRecyclerView.removeAllViews()
        }
    }
    private fun sendChatToOpenAIAndRetrieveResponse() {
        // assistant typing message "..."
        addMessageToChatRecyclerView(Message("assistant", "..."))
        // messages without "..." and with the system message as first
        val messagesOpenAI : ArrayList<Message> = ArrayList()
        val systemContent = "Keep responses short"
        if (systemContent.isNotEmpty()) {
            messagesOpenAI.add(0, Message("system", systemContent))

        }
        messagesOpenAI.removeLast()
       /* val requestData = mapOf(
            "model" to "gpt-3.5-turbo",
            "messages" to messagesOpenAI
        )*/
        /*with(viewModel) {
            val chatRequestDTO = ChatRequestDTO(model = "gpt-3.5-turbo",
                messages = messages.map { item-> MessageRequestItemDTO(item.role, item.content) }
            )
            sendChatToOpenAI(chatRequestDTO = chatRequestDTO)
        }
        viewModel.uiSendChatModel.collectWhenCreated {
            binding.loadingProgress.isVisible = it.isLoading
            if (it.data != null) {
                binding.loadingProgress.isVisible = false
                val choice = it.data.choices?.get(0)
                if (choice != null) {
                    Timber.d(">>>SUCCESS : ${choice.message?.role} - ${choice.message?.content}")
                    //removeLastMessageFromChatRecyclerView()
                    textToSpeech.stop()
                    addMessageToChatRecyclerView(Message("assistant", choice.message?.content.toString()))
                }
            }else{
                binding.loadingProgress.isVisible = false
                //removeLastMessageFromChatRecyclerView()
                Timber.d(">>>ERROR :")
            }
        }*/


        /*val headers = Headers.Builder()
            .add("Authorization", "Bearer ${binding.OpenAIAPIKeyEditText.text}")
            .add("Content-Type", "application/json")
            .build()

        // messages without "..." and with the system message as first
        val messagesOpenAI = ArrayList(messages)
        val systemContent = "Keep responses short"
        if (systemContent.isNotEmpty()) {
            messagesOpenAI.add(0, Message("system", systemContent))
        }
        messagesOpenAI.removeLast()

        val requestData = mapOf(
            "model" to "gpt-3.5-turbo",
            "messages" to messagesOpenAI
        )

        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = Gson().toJson(requestData).toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .headers(headers)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // if the request fails, show the error message to the user
                runOnUiThread {
                    removeLastMessageFromChatRecyclerView()
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val responseJson = Gson().fromJson(responseBody, ResponseJson::class.java)

                    val assistantContent = responseJson.choices[0].message.content
                    tokenUsage += responseJson.usage.total_tokens

                    runOnUiThread {
                        removeLastMessageFromChatRecyclerView()
                        textToSpeech.stop()
                        addMessageToChatRecyclerView(Message("assistant", assistantContent))
                        updateUsageTextView()
                    }

                    // update tokenUsage
                    val userRef = database.getReference("users/${auth.currentUser?.uid}/token_usage")
                    userRef.get()
                        .addOnSuccessListener {
                            userRef.setValue(tokenUsage)
                        }
                        .addOnFailureListener {
                            Log.e("Error updating tokenUsage", it.message!!)
                        }
                } else {
                    // if the response fails, show the error message to the user
                    runOnUiThread {
                        removeLastMessageFromChatRecyclerView()
                        Toast.makeText(this@MainActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })*/
    }
    private fun addMessageToChatRecyclerView(message: Message) {
        messages.add(message)

        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1)

        // speak message if it's from the assistant
        /*if (message.role == "assistant" && message.content != "...") {
            Timber.d(">>>Speak message")
            textToSpeech.speak(message.content, TextToSpeech.QUEUE_FLUSH, null, null)
        }else{
            Timber.d(">>>MUTE message")
        }*/
        //Mock
        if (message.role == "assistant"){
            textToSpeech.speak("How are you", TextToSpeech.QUEUE_FLUSH, null, null)
        }

    }
    private fun removeLastMessageFromChatRecyclerView() {
        messages.removeLast()
        chatAdapter.notifyItemRemoved(messages.size)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == RECORD_AUDIO_PERMISSION_CODE) && (grantResults.isNotEmpty()) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startListening()
        } else {
            // user wants to speak but denied record audio permission ¯\_(ツ)_/¯
        }
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizer.startListening(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    override fun onPause() {
        super.onPause()
        speechRecognizer.stopListening()
        textToSpeech.stop()
    }
    companion object{
        private const val RECORD_AUDIO_PERMISSION_CODE = 1
    }
}