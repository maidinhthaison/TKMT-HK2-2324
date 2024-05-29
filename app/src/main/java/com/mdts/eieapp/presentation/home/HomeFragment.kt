package com.mdts.eieapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.mdts.eieapp.base.BaseFragment
import com.mdts.eieapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun initBindingObject(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    /*private fun gotoDetailScreen(movieItem: MovieItem) {
        val bundle = Bundle().apply {
            this.putSerializable(MOVIE_ITEM_MODEL, movieItem)
        }
        findNavController().navigate(R.id.action_homeFragment_to_fragmentMovieDetail, bundle)
    }
*/
    companion object{
        const val MOVIE_ITEM_MODEL = "MovieItemModel"
    }
}