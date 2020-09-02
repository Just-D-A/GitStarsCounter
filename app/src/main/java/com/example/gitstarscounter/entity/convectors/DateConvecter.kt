package com.example.gitstarscounter.entity.convectors

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.toString()
    }
}