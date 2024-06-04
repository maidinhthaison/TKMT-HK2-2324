package com.mdts.eieapp.presentation.bottomsheet

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mdts.eieapp.R
import com.mdts.eieapp.base.BaseBottomSheetDialogFragment
import com.mdts.eieapp.base.collectWhenOwnerStarted
import com.mdts.eieapp.data.dto.chat.ChatRequestDTO
import com.mdts.eieapp.data.dto.chat.MessageRequestItemDTO
import com.mdts.eieapp.databinding.FragmentVocabularyBottomsheetBinding
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.presentation.chat.ChatFragment.Companion.MESSAGE_KEY_BUNDLE
import com.mdts.eieapp.presentation.chat.ChatViewModel
import com.mdts.eieapp.presentation.chat.Message
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Locale


@AndroidEntryPoint
class VocabularyBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<FragmentVocabularyBottomsheetBinding>() {

    // Text to Speech
    private lateinit var textToSpeech: TextToSpeech

    override val viewModel by viewModels<DictionaryViewModel>()

    override fun initBindingObject(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentVocabularyBottomsheetBinding {
        return FragmentVocabularyBottomsheetBinding.inflate(inflater, container, false)
    }

    override fun isExpandDialog(): Boolean {
        return true
    }

    override fun weightOfHeight(): Float? {
        return 0.7f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val offsetFromTop = 200
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            //expandedOffset = offsetFromTop
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        val message: Message? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable(
                    MESSAGE_KEY_BUNDLE,
                    Message::class.java
                )
            } else {
                arguments?.getSerializable(MESSAGE_KEY_BUNDLE) as Message

            }
        message?.let { bindData(it) }

        /* Text to Speech */
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(requireContext(), "Text to speech language not supported", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Text to speech initialization failed", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun bindData(message: Message){
        binding.tvWordLookUp.showSoftInputOnFocus = false
        binding.tvWord.text = String.format(getString(R.string.bottom_sheet_word_label), message.content)
        binding.ivVolume.setOnClickListener {
            textToSpeech.speak(message.content, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        translateText(message.content)

        binding.btnSearch.setOnClickListener {
            with(viewModel) {
                lookUp(binding.tvWordLookUp.text.toString().trim())
            }

        }
        /*viewModel.uiLookUpModel.collectWhenCreated { it ->
            binding.loadingProgress.isVisible = it.isLoading
            if (it.data != null) {
                binding.loadingProgress.isVisible = false
                val listResult = it.data
                listResult.forEach { it ->
                    val phoneTics = it.phoneticsList
                    phoneTics?.forEach { it1 ->
                        Timber.d(">>> :${it1.text} - ${it1.audio}")
                    }
                    val meanings = it.meaningsList
                    meanings?.forEach { it2 ->
                        Timber.d(">>> :${it2.partOfSpeech}")
                        val definitions = it2.definitions
                        definitions?.forEach { it3 -> Timber.d("Definition: ${it3.definition}") }
                    }
                }
            }else{
                binding.loadingProgress.isVisible = false
                Timber.d(">>>ERROR LOOKUP NULL")
            }
        }*/
        viewModel.uiLookUpModel.collectWhenOwnerStarted(this) {
            when (it) {
                TaskResult.Loading -> {
                    binding.loadingProgress.isVisible = true
                }

                is TaskResult.Success -> {
                    binding.loadingProgress.isVisible = false
                    val listResult = it.data
                    listResult.forEach { it ->
                        val phoneTics = it.phoneticsList
                        phoneTics?.forEach { it1 ->
                            Timber.d(">>> :${it1.text} - ${it1.audio}")
                        }
                        val meanings = it.meaningsList
                        meanings?.forEach { it2 ->
                            Timber.d(">>> :${it2.partOfSpeech}")
                            val definitions = it2.definitions
                            definitions?.forEach { it3 -> Timber.d("Definition: ${it3.definition}") }
                        }
                    }
                }

                is TaskResult.Failure -> {
                    binding.loadingProgress.isVisible = false
                    Timber.d(">>>>${it.error()?.getErrorMessage()}")
                }

                else -> {}
            }
        }
    }
    private fun translateText(text: String) {
        val options: TranslatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.VIETNAMESE)
            .build()

        val englishVietnameseTranslator: Translator = Translation.getClient(options!!)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        englishVietnameseTranslator.downloadModelIfNeeded(conditions).apply {
            addOnSuccessListener {
                englishVietnameseTranslator.translate(text).addOnSuccessListener {
                    binding.tvMeaning.text = String.format(getString(R.string.bottom_sheet_meaning_label),it)

                }
            }
            addOnFailureListener { e -> Timber.d("${e.printStackTrace()}")}
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    override fun onPause() {
        super.onPause()
        textToSpeech.stop()
    }

}