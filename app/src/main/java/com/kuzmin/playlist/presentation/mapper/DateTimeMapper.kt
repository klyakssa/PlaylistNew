package com.kuzmin.playlist.presentation.mapper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Time{
    const val YEAR = "yyyy"
    const val MINUTES_SECONDS = "mm:ss"
}
object DateTimeUtil {
    private val dateFormat = SimpleDateFormat(Time.YEAR, Locale.getDefault())
    private val timeFormat = SimpleDateFormat(Time.MINUTES_SECONDS, Locale.getDefault())

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