package com.kuzmin.playlist.data.db.converters

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeMapper {
    private const val YEAR = "yyyy"
    private const val MINUTES_SECONDS = "mm:ss"
    private val dateFormat = SimpleDateFormat(YEAR, Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun formatDate(date: String): Date {
        return SimpleDateFormat(YEAR).parse(date)
    }

}