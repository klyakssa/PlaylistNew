package com.kuzmin.playlist.presentation.mapper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeMapper {
    private const val YEAR = "yyyy"
    private const val MINUTES_SECONDS = "mm:ss"
    private val dateFormat = SimpleDateFormat(YEAR, Locale.getDefault())
    private val timeFormat = SimpleDateFormat(MINUTES_SECONDS, Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun formatTime(time: Long): String {
        return timeFormat.format(time)
    }

    fun formatTime(time: Int): String {
        return timeFormat.format(time)
    }
}