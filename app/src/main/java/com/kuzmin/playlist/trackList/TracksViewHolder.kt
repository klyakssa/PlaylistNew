package com.kuzmin.playlist.trackList

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kuzmin.playlist.R

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTrack: TextView = itemView.findViewById(R.id.titleTrack)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val timeSong: TextView = itemView.findViewById(R.id.timeSong)
    private val buttonArrowTrack: TextView = itemView.findViewById(R.id.buttonArrowTrack)
    private val sourceImage: ImageView = itemView.findViewById(R.id.sourceImage)

    fun bind(track: Track) {
        val sb = StringBuilder()
        titleTrack.text = track.trackName
        artistName.text = track.artistName
        timeSong.text = track.trackTime
        buttonArrowTrack.setOnClickListener{

        }
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f,itemView.context)))
            .placeholder(R.drawable.ic_placeholder)
            .into(sourceImage)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}