package com.kuzmin.playlist.ui.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.R
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.databinding.ActivityPlayerBinding
import com.kuzmin.playlist.domain.repository.MediaPlayerListener
import com.kuzmin.playlist.presentation.mapper.ArtworkMapper
import com.kuzmin.playlist.presentation.mapper.DateTimeMapper
import com.kuzmin.playlist.presentation.model.TrackPlayerInfo

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: TrackPlayerInfo

    private val workWithMediaPlayerUseCase = Creator.provideWorkWithMediaPlayerUseCase()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Gson().fromJson<TrackPlayerInfo>(
            intent.getStringExtra(Preferences.TRACK_TO_ARRIVE.pref),
            TrackPlayerInfo::class.java
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
        track.previewUrl
        //preparePlayer()
        workWithMediaPlayerUseCase.playerPrepare(
            previewUrl = track.previewUrl,
            listener = object : MediaPlayerListener {
            override fun preparedPlayer() {
                binding.buttonPlay.isEnabled = true
            }

            override fun completionPlayer() {
                binding.timeNow.text = getString(R.string.testTimeNow)
                binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_play_button,
                    0,
                    0,
                    0
                )
            }

            override fun startPlayer() {
                binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_pause_button,
                    0,
                    0,
                    0
                )
            }

            override fun pausePlayer() {
                binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_play_button,
                    0,
                    0,
                    0
                )
            }

            override fun currentTimeMusic(timeInt: Int) {
                binding.timeNow.text = DateTimeMapper.formatTime(timeInt)
            }
        }
        )

        binding.buttonPlay.setOnClickListener {
            workWithMediaPlayerUseCase.playStartControl()
        }

    }


    override fun onPause() {
        super.onPause()
        workWithMediaPlayerUseCase.hardPauseToPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        workWithMediaPlayerUseCase.releasePlayer()
    }

}