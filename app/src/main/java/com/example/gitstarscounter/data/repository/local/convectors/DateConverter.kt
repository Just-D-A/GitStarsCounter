package com.example.gitstarscounter.data.repository.local.convectors

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DateConverter {
    private val simpleDateFormate: SimpleDateFormat =
        SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    @TypeConverter
    fun convertDateToTimestamp(date: Date?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun convertTimestampToDate(date: String?): Date? {
        return simpleDateFormate.parse(date)
    }
}
