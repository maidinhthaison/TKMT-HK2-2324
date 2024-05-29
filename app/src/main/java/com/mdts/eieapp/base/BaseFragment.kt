package com.mdts.eieapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.movedb.base.collectLatestWhenOwnerStarted
import com.example.movedb.base.collectWhenOwnerCreated
import com.example.movedb.base.collectWhenOwnerResumed
import com.example.movedb.base.collectWhenOwnerStarted
import kotlinx.coroutines.flow.Flow

abstract class BaseFragment <T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    private val binding get() = _binding!!

    open val viewModel: BaseViewModel? get() = null

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

    fun <T> Flow<T>.collectLatestWhenStarted(action: suspend (T) -> Unit) {
        collectLatestWhenOwnerStarted(viewLifecycleOwner, action)
    }

    fun <T> Flow<T>.collectWhenResumed(action: suspend (T) -> Unit) {
        collectWhenOwnerResumed(viewLifecycleOwner, action)
    }

    fun <T> Flow<T>.collectWhenCreated(action: suspend (T) -> Unit) {
        collectWhenOwnerCreated(viewLifecycleOwner, action)
    }
}


