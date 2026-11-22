package com.mathra.clockcalendar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ReminderRepeat { NONE, DAILY, YEARLY_BIRTHDAY, YEARLY_ANNIVERSARY }

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val note: String = "",
    val epochMillis: Long,       // date + time of the reminder
    val repeat: ReminderRepeat = ReminderRepeat.NONE,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
