package com.kuzmin.playlist.presentation.library.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.FragmentPlaylistBinding
import com.kuzmin.playlist.presentation.library.Fragments.view_models.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment() {

    private val playlistViewModel by viewModel<PlaylistViewModel>()

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nothing.setImageResource(R.drawable.ic_nothing)
        binding.playlistText.text = getString(R.string.playlistText)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}