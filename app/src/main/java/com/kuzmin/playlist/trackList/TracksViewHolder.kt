package com.kuzmin.playlist.trackList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuzmin.playlist.R

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTrack: TextView = itemView.findViewById(R.id.titleTrack)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val buttonArrowTrack: TextView = itemView.findViewById(R.id.buttonArrowTrack)
    private val sourceImage: ImageView = itemView.findViewById(R.id.sourceImage)

    fun bind(track: Track) {
        titleTrack.text = track.trackName
        artistName.text = track.artistName
        buttonArrowTrack.setOnClickListener{

        }
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.ic_placeholder)
            .into(sourceImage)
    }
}