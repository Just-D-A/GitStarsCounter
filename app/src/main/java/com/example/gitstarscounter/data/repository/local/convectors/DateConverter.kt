package com.example.gitstarscounter.data.repository.local.convectors

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object DateConverter {
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    @JvmStatic
    @TypeConverter
    fun convertDateToTimestamp(date: Date?): String? {
        return date?.toString()
    }

    @JvmStatic
    @TypeConverter
    fun convertTimestampToDate(date: String?): Date? {
        return simpleDateFormat.parse(date)
    }
}
