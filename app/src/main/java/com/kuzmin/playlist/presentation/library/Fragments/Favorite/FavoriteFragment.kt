package com.kuzmin.playlist.presentation.library.Fragments.Favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.FragmentFavoriteBinding
import com.kuzmin.playlist.presentation.library.Fragments.model.FavoriteState
import com.kuzmin.playlist.presentation.library.Fragments.Favorite.view_models.FavoriteViewModel
import com.kuzmin.playlist.presentation.main.RootActivity
import com.kuzmin.playlist.presentation.models.Track
import com.kuzmin.playlist.presentation.search.TracksListAdapter
import com.kuzmin.playlist.presentation.utils.debounce
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val favoriteViewModel by viewModel<FavoriteViewModel>()

    private lateinit var binding: FragmentFavoriteBinding

    private lateinit var onPlayerDebounce: (Track) -> Unit


    private val tracksList = ArrayList<Track>()
    private val tracksAdapter = TracksListAdapter{_,it ->
        if (favoriteViewModel.clickDebounce()) {
            (activity as RootActivity).animateBottomNavigationView(View.GONE)
            onPlayerDebounce(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tracksAdapter.data = tracksList
        binding.tracksList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksList.adapter = tracksAdapter

        onPlayerDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope,  Dispatchers.Main) { track ->
            findNavController().navigate(R.id.action_libraryFragment_to_playerActivity, bundleOf(
                TRACK_TO_ARRIVE to Gson().toJson(track)))
        }

        binding.nothing.setImageResource(R.drawable.ic_nothing)
        binding.favoriteText.text = getString(R.string.favoriteText)
        favoriteViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getTracks()
    }

    private fun render(state: FavoriteState) {
        when(state) {
            is FavoriteState.isNotTracks -> {
                binding.nothing.visibility = View.VISIBLE
                binding.favoriteText.visibility = View.VISIBLE
                binding.tracksList.visibility = View.GONE
            }
            is FavoriteState.isTracks -> {
                binding.nothing.visibility = View.GONE
                binding.favoriteText.visibility = View.GONE
                binding.tracksList.visibility = View.VISIBLE
                tracksList.clear()
                tracksList.addAll(state.tracks.reversed())
                tracksAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun newInstance() =
            FavoriteFragment().apply {
                arguments = bundleOf()
                tag
            }
        private const val CLICK_DEBOUNCE_DELAY = 300L
        const val TRACK_TO_ARRIVE = "track"
    }
}