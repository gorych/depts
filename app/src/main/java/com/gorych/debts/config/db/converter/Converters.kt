package com.gorych.debts.config.db.converter

import androidx.room.TypeConverter
import com.gorych.debts.good.Good
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

    @TypeConverter
    fun fromGoodMeasurementUnit(unit: Good.MeasurementUnit): String {
        return unit.name
    }

    @TypeConverter
    fun toGoodMeasurementUnit(unitName: String?): Good.MeasurementUnit? {
        return unitName?.let { Good.MeasurementUnit.valueOf(unitName) }
    }
}