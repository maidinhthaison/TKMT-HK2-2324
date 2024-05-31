package com.mdts.eieapp.base

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.Flow

abstract class BaseBottomSheetDialogFragment<T : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    open val viewModel: BaseViewModel? get() = null

    protected open fun isExpandDialog() = false

    protected open fun weightOfHeight(): Float? = null

    protected open fun getTitle(): Int? = null

    protected open fun roundedCorner(): Boolean = false

    private fun getWindowHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    abstract fun initBindingObject(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initBindingObject(inflater, container, savedInstanceState)
        if (isExpandDialog()) {
            dialog?.setOnShowListener { dialog ->
                val bottomSheet: FrameLayout? = (dialog as? BottomSheetDialog)
                    ?.findViewById(com.google.android.material.R.id.design_bottom_sheet)

                weightOfHeight()?.let { percent ->
                    val param = bottomSheet?.layoutParams?.apply {
                        height = (getWindowHeight() * percent).toInt()
                    }
                    bottomSheet?.layoutParams = param
                }

                bottomSheet?.let {
                    BottomSheetBehavior.from(bottomSheet).state =
                        if (!roundedCorner()) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HALF_EXPANDED

                    if (roundedCorner()) {
                        BottomSheetBehavior.from(bottomSheet).halfExpandedRatio =
                            weightOfHeight() ?: 0.5f
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    /**
     * Safe collect flow
     */

    fun <T> Flow<T>.collectWhenStarted(action: suspend (T) -> Unit) {
        collectWhenOwnerStarted(viewLifecycleOwner, action)
    }

    fun <T> Flow<T>.collectWhenResumed(action: suspend (T) -> Unit) {
        collectWhenOwnerResumed(viewLifecycleOwner, action)
    }

    fun <T> Flow<T>.collectWhenCreated(action: suspend (T) -> Unit) {
        collectWhenOwnerCreated(viewLifecycleOwner, action)
    }
}

