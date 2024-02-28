package com.kuzmin.playlist.presentation.library.Fragments.Playlist

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.Target
import com.kuzmin.playlist.R
import com.kuzmin.playlist.presentation.models.Playlist
import java.io.File


class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val img: ImageView = itemView.findViewById(R.id.image)
    private val name: TextView = itemView.findViewById(R.id.playlistName)
    private val count: TextView = itemView.findViewById(R.id.countTracks)

    fun bind(playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.imgFilePath.toUri())
            .fitCenter()
            .transform(RoundedCorners(dpToPx(8f,itemView.context)))
            .placeholder(R.drawable.ic_placeholder_playlist)
            .into(img)

        name.text = playlist.playlistName
        count.text = playlist.countTracks.toString() + " треков"
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}