package com.kuzmin.playlist.presentation.audioplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.ActivityPlayerBinding
import com.kuzmin.playlist.domain.model.PlaylistDto
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.presentation.audioplayer.bottom_sheet.PlaylistAdapter
import com.kuzmin.playlist.presentation.audioplayer.model.PlayerState
import com.kuzmin.playlist.presentation.audioplayer.view_model.PlayerViewModel
import com.kuzmin.playlist.presentation.mapper.ArtworkMapper
import com.kuzmin.playlist.presentation.mapper.DateTimeMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerActivity : Fragment() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: TrackDto

    private var isClickAllowed = true

    private val viewModel: PlayerViewModel by viewModel<PlayerViewModel>() {
        parametersOf(track.previewUrl)
    }

    val bottomSheetBehavior: BottomSheetBehavior<*> by lazy {
        BottomSheetBehavior.from(binding.standardBottomSheet)
    }

    private val playlistList = ArrayList<PlaylistDto>()
    private val playlistAdapter = PlaylistAdapter{_, it ->
            viewModel.addTrackInPlaylist(it, track.trackId)
    }


    companion object {
        const val TRACK_TO_ARRIVE = "track"
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        track = Gson().fromJson<TrackDto>(
            arguments?.getString(TRACK_TO_ARRIVE),
            TrackDto::class.java
        )

        viewModel.initFav(track.trackId)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        Glide.with(requireContext())
            .load(ArtworkMapper.getCoverArtwork(track.artworkUrl100))
            .fitCenter()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_8dp)))
            .placeholder(R.drawable.ic_placeholder_player)
            .into(binding.cover)

        binding.album.text = track.collectionName
        binding.artist.text = track.artistName
        binding.time.text = DateTimeMapper.formatTime(track.trackTime.toLong())
        binding.albumToo.text = track.collectionName
        binding.year.text = DateTimeMapper.formatDate(track.releaseDate)
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country

        binding.buttonPlay.setOnClickListener {
            viewModel.playStartControl()
        }

        binding.buttonLike.setOnClickListener {
            if (clickDebounce()){
                viewModel.addOrDelTrack(track)
            }
        }

        binding.buttonPlaylist.setOnClickListener {
            if (clickDebounce()){
                viewModel.getPlaylists()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerActivity_to_addPlaylist2)
        }

        playlistAdapter.data = playlistList
        binding.playlistList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.playlistList.adapter = playlistAdapter

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.main.alpha = 1f
                    }
                    else -> {
                        binding.main.alpha = 0.5f
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.Completion -> {
                binding.timeNow.text = getString(R.string.testTimeNow)
                binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_play_button,
                    0,
                    0,
                    0
                )
            }
            is PlayerState.CurrentTime -> {
                binding.timeNow.text = DateTimeMapper.formatTime(state.timeInt)
            }
            is PlayerState.Pause -> {
                binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_play_button,
                    0,
                    0,
                    0
                )
            }
            is PlayerState.Prepared -> {
                binding.buttonPlay.isEnabled = true
            }
            is PlayerState.Start -> {
                binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_pause_button,
                    0,
                    0,
                    0
                )
            }
            is PlayerState.Liked -> {
                binding.buttonLike.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_favorite_button_click,
                    0,
                    0,
                    0
                )
            }
            is PlayerState.NotLiked -> {
                binding.buttonLike.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_favorite_button,
                    0,
                    0,
                    0
                )
            }
            is PlayerState.isNotPlaylist -> {
                binding.playlistList.visibility = View.GONE
            }
            is PlayerState.isPlaylist -> {
                binding.playlistList.visibility = View.VISIBLE
                playlistList.clear()
                playlistList.addAll(state.playlist)
                playlistAdapter.notifyDataSetChanged()
            }
            is PlayerState.notInPlaylist -> {
                Toast.makeText(requireContext(), "Добавлено в плейлист ${state.playlistName}", Toast.LENGTH_SHORT).show()
            }
            is PlayerState.isInPlaylist -> {
                Toast.makeText(requireContext(), "Трек уже добавлен в плейлист ${state.playlistName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(PlayerActivity.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releseMediaPlayer()
    }


}