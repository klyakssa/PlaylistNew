package com.kuzmin.playlist.presentation.library.Fragments.Favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.model.TrackDto
import java.util.*

class TracksListAdapter(val clickListener: TrackClickListener) : RecyclerView.Adapter<TracksViewHolder> () {

    var data = ArrayList<TrackDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.onLocationClick(this, data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun interface TrackClickListener {
        fun onLocationClick(adapter: TracksListAdapter, track: TrackDto)
    }

}
