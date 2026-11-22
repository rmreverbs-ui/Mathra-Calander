package com.mathra.clockcalendar.util

import com.mathra.clockcalendar.data.local.ReminderEntity
import com.mathra.clockcalendar.data.local.ReminderRepeat
import java.time.Instant
import java.time.temporal.ChronoUnit

/** Works out when a reminder should next fire, including daily / yearly repeats. */
object ReminderTime {

    /**
     * Next trigger strictly after [afterMillis], or null when a one-time reminder
     * is already in the past (nothing left to schedule).
     */
    fun nextTrigger(reminder: ReminderEntity, afterMillis: Long = System.currentTimeMillis()): Long? {
        val base = Instant.ofEpochMilli(reminder.epochMillis).atZone(DateUtils.SRI_LANKA_ZONE)
        return when (reminder.repeat) {
            ReminderRepeat.NONE ->
                if (reminder.epochMillis > afterMillis) reminder.epochMillis else null

            ReminderRepeat.DAILY -> {
                if (reminder.epochMillis > afterMillis) return reminder.epochMillis
                val after = Instant.ofEpochMilli(afterMillis).atZone(DateUtils.SRI_LANKA_ZONE)
                var days = ChronoUnit.DAYS.between(base, after)
                var candidate = base.plusDays(days)
                while (candidate.toInstant().toEpochMilli() <= afterMillis) {
                    days++
                    candidate = base.plusDays(days)
                }
                candidate.toInstant().toEpochMilli()
            }

            ReminderRepeat.YEARLY_BIRTHDAY, ReminderRepeat.YEARLY_ANNIVERSARY -> {
                if (reminder.epochMillis > afterMillis) return reminder.epochMillis
                val after = Instant.ofEpochMilli(afterMillis).atZone(DateUtils.SRI_LANKA_ZONE)
                var years = ChronoUnit.YEARS.between(base, after)
                // plusYears clamps Feb 29 to Feb 28 in non-leap years, so birthdays never get skipped.
                var candidate = base.plusYears(years)
                while (candidate.toInstant().toEpochMilli() <= afterMillis) {
                    years++
                    candidate = base.plusYears(years)
                }
                candidate.toInstant().toEpochMilli()
            }
        }
    }
}
