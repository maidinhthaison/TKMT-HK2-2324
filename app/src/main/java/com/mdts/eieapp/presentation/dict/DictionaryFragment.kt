package com.mdts.eieapp.presentation.dict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdts.eieapp.base.BaseFragment
import com.mdts.eieapp.databinding.FragmentDictionaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DictionaryFragment :
    BaseFragment<FragmentDictionaryBinding>() {
    override fun initBindingObject(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDictionaryBinding {
        return FragmentDictionaryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}