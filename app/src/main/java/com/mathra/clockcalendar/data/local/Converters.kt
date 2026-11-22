package com.mathra.clockcalendar.data.local

import androidx.room.TypeConverter

/** Room type converters for the small set of non-primitive fields we persist. */
class Converters {
    @TypeConverter
    fun fromRepeat(value: ReminderRepeat): String = value.name

    @TypeConverter
    fun toRepeat(value: String): ReminderRepeat = ReminderRepeat.valueOf(value)

    @TypeConverter
    fun fromDaySet(days: Set<Int>): String = days.joinToString(",")

    @TypeConverter
    fun toDaySet(value: String): Set<Int> =
        if (value.isBlank()) emptySet() else value.split(",").map { it.toInt() }.toSet()
}
