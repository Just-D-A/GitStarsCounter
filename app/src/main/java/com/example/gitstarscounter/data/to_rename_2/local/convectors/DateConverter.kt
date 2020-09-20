package com.example.gitstarscounter.data.to_rename_2.local.convectors

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DateConverter {
    @TypeConverter
    fun convertDateToTimestamp(date: Date?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun convertTimestampToDate(date: String?): Date? {
        return sdf3.parse(date)
    }

    companion object {
        val sdf3: SimpleDateFormat =
            SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    }
}
