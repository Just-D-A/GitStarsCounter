package com.example.gitstarscounter.data.local.convectors

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.toString()
    }

    companion object {
        val sdf3: SimpleDateFormat =
            SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    }
}
