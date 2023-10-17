package com.kuzmin.playlist

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.kuzmin.playlist.databinding.ActivityPlayerBinding
import com.kuzmin.playlist.trackList.Track
import kotlinx.coroutines.handleCoroutineException
import java.text.DateFormat
import java.text.DateFormat.Field.YEAR
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private var mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var runnable: Runnable
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_DEBOUNCE_DELAY = 2000L
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Gson().fromJson<Track>(intent.getStringExtra(Const.TRACK_TO_ARRIVE.const), Track::class.java)

        binding.back.setOnClickListener {
            finish()
        }

        Glide.with(applicationContext)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(dpToPx(8f,applicationContext)))
            .placeholder(R.drawable.ic_placeholder_player)
            .into(binding.cover)

        binding.album.text = track.collectionName
        binding.artist.text = track.artistName
        binding.time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toLong())
        binding.albumToo.text = track.collectionName
        binding.year.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate)
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country

        preparePlayer()

        binding.buttonPlay.setOnClickListener {
            playbackControl()
        }

        runnable = object : Runnable {
            override fun run() {
                binding.timeNow.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIME_DEBOUNCE_DELAY)
            }
        }
        handler.postDelayed(runnable,0)
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_button, 0, 0, 0);
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_button, 0, 0, 0);
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacks(runnable)
            binding.timeNow.text = getString(R.string.testTimeNow)
            binding.buttonPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_button, 0, 0, 0);
            playerState = STATE_PREPARED
        }
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}