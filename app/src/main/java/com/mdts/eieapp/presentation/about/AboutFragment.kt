package com.mdts.eieapp.presentation.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mdts.eieapp.base.BaseFragment
import com.mdts.eieapp.databinding.FragmentAboutBinding
import com.mdts.eieapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment :
    BaseFragment<FragmentAboutBinding>() {
    override fun initBindingObject(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }
}