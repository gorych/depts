package com.gorych.debts.config.db.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(textDate: String?): LocalDateTime? {
        return textDate?.let { LocalDateTime.parse(textDate) }
    }

}