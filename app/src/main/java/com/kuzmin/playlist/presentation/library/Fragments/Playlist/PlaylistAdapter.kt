package com.kuzmin.playlist.presentation.library.Fragments.Playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.model.PlaylistDto
import java.util.ArrayList

class PlaylistAdapter(val clickListener: PlaylistClickListener): RecyclerView.Adapter<PlaylistViewHolder>() {

    var data = ArrayList<PlaylistDto>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.onLocationClick(this, data[position])
        }
    }

    fun interface PlaylistClickListener {
        fun onLocationClick(adapter: PlaylistAdapter, playlist: PlaylistDto)
    }
}