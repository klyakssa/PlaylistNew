package com.kuzmin.playlist.presentation.library.Fragments.Playlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.FragmentPlaylistBinding
import com.kuzmin.playlist.presentation.audioplayer.view_model.PlayerViewModel
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.view_models.PlaylistViewModel
import com.kuzmin.playlist.presentation.library.Fragments.model.FavoriteState
import com.kuzmin.playlist.presentation.library.Fragments.model.PlaylistState
import com.kuzmin.playlist.presentation.main.RootActivity
import com.kuzmin.playlist.presentation.models.Playlist
import com.kuzmin.playlist.presentation.utils.debounce
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel<PlaylistViewModel>()

    private lateinit var binding: FragmentPlaylistBinding

    private lateinit var onCreatePlaylistDebounce: () -> Unit

    private val playlistList = ArrayList<Playlist>()
    private val playlistAdapter = PlaylistAdapter{_, _ ->

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nothing.setImageResource(R.drawable.ic_nothing)
        binding.playlistText.text = getString(R.string.playlistText)

        onCreatePlaylistDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope,  Dispatchers.Main) {
            findNavController().navigate(R.id.action_libraryFragment_to_addPlaylist, bundleOf("from" to 2))
        }

        binding.newPlaylist.setOnClickListener {
            (activity as RootActivity).animateBottomNavigationView(View.GONE)
            onCreatePlaylistDebounce()
        }

        playlistAdapter.data = playlistList
        binding.playlistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsList.adapter = playlistAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.isNotPlaylist -> {
                binding.nothing.visibility = View.VISIBLE
                binding.playlistText.visibility = View.VISIBLE
                binding.playlistsList.visibility = View.GONE
            }
            is PlaylistState.isPlaylist -> {
                binding.nothing.visibility = View.GONE
                binding.playlistText.visibility = View.GONE
                binding.playlistsList.visibility = View.VISIBLE
                playlistList.clear()
                playlistList.addAll(state.playlist)
                playlistAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun newInstance() =
            PlaylistFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}