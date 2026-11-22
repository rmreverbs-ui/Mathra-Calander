package com.mathra.clockcalendar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val hour: Int,                 // 0-23
    val minute: Int,                // 0-59
    val repeatDays: Set<Int> = emptySet(), // Calendar.SUNDAY..SATURDAY, empty = one-time
    val isEnabled: Boolean = true,
    val soundUri: String? = null,
    val vibrate: Boolean = true
)
