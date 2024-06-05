package com.mdts.eieapp.presentation.bottomsheet

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
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
import com.mdts.eieapp.databinding.FragmentVocabularyBottomsheetBinding
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.presentation.bottomsheet.adapter.MeaningsAdapter
import com.mdts.eieapp.presentation.bottomsheet.adapter.PhoneticsAdapter
import com.mdts.eieapp.presentation.chat.ChatFragment.Companion.MESSAGE_KEY_BUNDLE
import com.mdts.eieapp.presentation.chat.Message
import com.mdts.eieapp.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Locale


@AndroidEntryPoint
class VocabularyBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<FragmentVocabularyBottomsheetBinding>() {

    // Text to Speech
    private lateinit var textToSpeech: TextToSpeech

    override val viewModel by viewModels<DictionaryViewModel>()

    private lateinit var phoneticsAdapter: PhoneticsAdapter
    private lateinit var meaningsAdapter: MeaningsAdapter

    override fun initBindingObject(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentVocabularyBottomsheetBinding {
        return FragmentVocabularyBottomsheetBinding.inflate(inflater, container, false)
    }

    override fun isExpandDialog(): Boolean {
        return true
    }

    override fun weightOfHeight(): Float? {
        return 0.8f
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
                    ToastUtils.showToast(requireContext(), R.string.t2p_language_not_support_error, 0)
                }
            } else {
                ToastUtils.showToast(requireContext(), R.string.t2p_language_initialization_error, 0)
            }
        }

        /**
         * setup recycle view
         */

        phoneticsAdapter =  PhoneticsAdapter(context = requireContext())
        binding.rvPhonetics.apply {

            adapter = phoneticsAdapter
            itemAnimator = DefaultItemAnimator()
        }

        meaningsAdapter =  MeaningsAdapter(context = requireContext())
        binding.rvMeaning.apply {

            adapter = meaningsAdapter
            itemAnimator = DefaultItemAnimator()
        }

    }
    private fun bindData(message: Message){
        binding.tvWord.text = String.format(getString(R.string.bottom_sheet_word_label), message.content)
        binding.ivVolume.setOnClickListener {
            textToSpeech.speak(message.content, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        translateText(message.content)

        binding.btnSearch.setOnClickListener {
            with(viewModel) {
                lookUp(binding.edtWordLookUp.text.toString().trim())
            }

        }

        viewModel.uiLookUpModel.collectWhenOwnerStarted(this) {
            when (it) {
                TaskResult.Loading -> {
                    binding.tvPhonetics.isVisible = false
                    binding.tvMeanings.isVisible = false
                    binding.loadingProgress.isVisible = true
                }

                is TaskResult.Success -> {
                    binding.loadingProgress.isVisible = false
                    binding.tvPhonetics.isVisible = true
                    binding.tvMeanings.isVisible = true
                    val listResult = it.data
                    listResult.forEach { it ->
                        val phoneTics = it.phoneticsList
                        val meanings = it.meaningsList
                        //binding recycler view
                        phoneticsAdapter.submitList(phoneTics)
                        meaningsAdapter.submitList(meanings)

                    }
                }

                is TaskResult.Failure -> {
                    binding.loadingProgress.isVisible = false
                    binding.tvPhonetics.isVisible = false
                    binding.tvMeanings.isVisible = false
                    ToastUtils.showToast(requireContext(), it.error()?.getErrorMessage() ?:
                            String.format(getString(R.string.look_up_word_error), binding.edtWordLookUp.text.toString().trim()),0)
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