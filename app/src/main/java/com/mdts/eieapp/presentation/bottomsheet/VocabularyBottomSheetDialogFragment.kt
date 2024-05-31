package com.mdts.eieapp.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mdts.eieapp.base.BaseBottomSheetDialogFragment
import com.mdts.eieapp.databinding.FragmentVocabularyBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VocabularyBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<FragmentVocabularyBottomsheetBinding>() {

    //private val historyDocumentViewModel by viewModels<HistoryDocumentViewModel>()


    override fun initBindingObject(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentVocabularyBottomsheetBinding {
        return FragmentVocabularyBottomsheetBinding.inflate(inflater, container, false)
    }

    override fun isExpandDialog(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val offsetFromTop = 200
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            expandedOffset = offsetFromTop
            //state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }


        /*with(historyDocumentViewModel) {
            documentItem?.id?.let { getListHistoryDocument(it) }
        }

        historyDocumentViewModel.uiGetHistoryDocumentModel.collectWhenStarted {
            binding.loadingProgress.isVisible = it.isLoading
            if (it.data != null) {
                listHistoryDocumentAdapter.submitList(it.data)
            }
        }

        binding.btnEdit.setOnClickListener {

            dismiss()
        }*/
    }

    private fun bindingData() {
    }

}