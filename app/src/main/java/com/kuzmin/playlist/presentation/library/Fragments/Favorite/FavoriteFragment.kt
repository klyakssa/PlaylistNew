package com.kuzmin.playlist.presentation.library.Fragments.Favorite

import android.content.Intent
import android.graphics.Path.Direction
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.FragmentFavoriteBinding
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.presentation.audioplayer.PlayerActivity
import com.kuzmin.playlist.presentation.library.Fragments.model.FavoriteState
import com.kuzmin.playlist.presentation.library.Fragments.Favorite.view_models.FavoriteViewModel
import com.kuzmin.playlist.presentation.library.LibraryFragment
import com.kuzmin.playlist.presentation.search.TracksListAdapter
import com.kuzmin.playlist.presentation.search.TracksSearchFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val favoriteViewModel by viewModel<FavoriteViewModel>()

    private lateinit var binding: FragmentFavoriteBinding

    private lateinit var navController: NavController


    private var isClickAllowed = true

    private val tracksList = ArrayList<TrackDto>()
    private val tracksAdapter = TracksListAdapter{_,it ->
        if (clickDebounce()) {
            navController.navigate(R.id.action_libraryFragment_to_playerActivity, bundleOf("track" to Gson().toJson(it)))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val navHostFragment = parentFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tracksAdapter.data = tracksList
        binding.tracksList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksList.adapter = tracksAdapter

        binding.nothing.setImageResource(R.drawable.ic_nothing)
        binding.favoriteText.text = getString(R.string.favoriteText)
        favoriteViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.initTracks()
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
                tracksList.addAll(state.tracks)
                tracksAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(FavoriteFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        fun newInstance() =
            FavoriteFragment().apply {
                arguments = bundleOf()
                tag
            }
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val TRACK_TO_ARRIVE = "track"
    }
}