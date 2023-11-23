package com.kuzmin.playlist.presentation.audioplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.R
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.databinding.ActivityPlayerBinding
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.domain.mediaplayer.repository.MediaPlayerRepository
import com.kuzmin.playlist.presentation.application.App
import com.kuzmin.playlist.presentation.audioplayer.model.PlayerState
import com.kuzmin.playlist.presentation.audioplayer.view_model.PlayerViewModel
import com.kuzmin.playlist.presentation.mapper.ArtworkMapper
import com.kuzmin.playlist.presentation.mapper.DateTimeMapper
import com.kuzmin.playlist.presentation.search.model.TracksState
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: TrackDto

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track.previewUrl))[PlayerViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Gson().fromJson<TrackDto>(
            intent.getStringExtra(Preferences.TRACK_TO_ARRIVE.pref),
            TrackDto::class.java
        )

        binding.back.setOnClickListener {
            finish()
        }

        Glide.with(applicationContext)
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

        viewModel.observeState().observe(this) {
            render(it)
        }

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
        }
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