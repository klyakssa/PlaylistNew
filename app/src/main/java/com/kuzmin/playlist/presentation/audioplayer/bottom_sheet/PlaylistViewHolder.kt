package com.kuzmin.playlist.presentation.audioplayer.bottom_sheet

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.Target
import com.kuzmin.playlist.R
import com.kuzmin.playlist.presentation.models.Playlist
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val trackItem: LinearLayout = itemView.findViewById(R.id.track_item)
    private val titleTrack: TextView = itemView.findViewById(R.id.titleTrack)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val timeSong: TextView = itemView.findViewById(R.id.timeSong)
    private val buttonArrowTrack: TextView = itemView.findViewById(R.id.buttonArrowTrack)
    private val sourceImage: ImageView = itemView.findViewById(R.id.sourceImage)
    private val dotImage: ImageView = itemView.findViewById(R.id.dotImage)

    fun bind(playlist: Playlist) {
        titleTrack.text =  playlist.playlistName
        artistName.text = playlist.countTracks.toString() + " треков"

        Glide.with(itemView)
            .load(playlist.imgFilePath.toUri())
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