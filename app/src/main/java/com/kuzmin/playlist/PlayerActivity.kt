package com.kuzmin.playlist

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.kuzmin.playlist.databinding.ActivityPlayerBinding
import com.kuzmin.playlist.trackList.Track
import java.text.DateFormat
import java.text.DateFormat.Field.YEAR
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
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

    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

}