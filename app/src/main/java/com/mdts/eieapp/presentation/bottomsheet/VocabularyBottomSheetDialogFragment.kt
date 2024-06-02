package com.mdts.eieapp.presentation.bottomsheet

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mdts.eieapp.R
import com.mdts.eieapp.base.BaseBottomSheetDialogFragment
import com.mdts.eieapp.databinding.FragmentVocabularyBottomsheetBinding
import com.mdts.eieapp.presentation.chat.ChatFragment.Companion.MESSAGE_KEY_BUNDLE
import com.mdts.eieapp.presentation.chat.Message
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Locale


@AndroidEntryPoint
class VocabularyBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<FragmentVocabularyBottomsheetBinding>() {

    // Text to Speech
    private lateinit var textToSpeech: TextToSpeech

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
        binding.tvWord.text = String.format(getString(R.string.bottom_sheet_word_label), message.content)
        binding.ivVolume.setOnClickListener {
            textToSpeech.speak(message.content, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        translateText(message.content)

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