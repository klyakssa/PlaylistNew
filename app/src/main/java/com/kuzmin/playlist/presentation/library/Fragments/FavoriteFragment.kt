package com.kuzmin.playlist.presentation.library.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.FragmentFavoriteBinding
import com.kuzmin.playlist.presentation.library.Fragments.view_models.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val favoriteViewModel by viewModel<FavoriteViewModel>()

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nothing.setImageResource(R.drawable.ic_nothing)
        binding.favoriteText.text = getString(R.string.favoriteText)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}