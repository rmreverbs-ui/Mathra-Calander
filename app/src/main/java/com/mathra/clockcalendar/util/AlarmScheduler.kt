package com.mathra.clockcalendar.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mathra.clockcalendar.data.local.AlarmEntity
import com.mathra.clockcalendar.receiver.AlarmReceiver
import java.util.Calendar

/** True when the OS lets this app set exact alarms (always true below Android 12 and on 13+). */
fun Context.canScheduleExact(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true
    val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    return am.canScheduleExactAlarms()
}

/**
 * Sets an exact alarm when allowed. If the user has not granted exact-alarm access
 * (Android 12 / 12L), it falls back to an inexact alarm so the notification still
 * arrives, just possibly a few minutes late, instead of silently never firing.
 */
fun AlarmManager.setBestEffort(context: Context, triggerAtMillis: Long, pi: PendingIntent) {
    if (context.canScheduleExact()) {
        setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
    } else {
        setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
    }
}

/** Schedules and cancels real OS-level alarms via AlarmManager (works in Doze mode). */
class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(alarm: AlarmEntity, afterMillis: Long = System.currentTimeMillis()) {
        alarmManager.setBestEffort(context, nextTriggerMillis(alarm, afterMillis), pendingIntentFor(alarm))
    }

    fun cancel(alarm: AlarmEntity) {
        alarmManager.cancel(pendingIntentFor(alarm))
    }

    private fun pendingIntentFor(alarm: AlarmEntity): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, alarm.id)
            putExtra(AlarmReceiver.EXTRA_LABEL, alarm.label)
        }
        return PendingIntent.getBroadcast(
            context, alarm.id.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /** Next epoch millis strictly after [afterMillis], honouring repeatDays if set. */
    private fun nextTriggerMillis(alarm: AlarmEntity, afterMillis: Long): Long {
        val candidate = Calendar.getInstance().apply {
            timeInMillis = afterMillis
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (alarm.repeatDays.isEmpty()) {
            if (candidate.timeInMillis <= afterMillis) candidate.add(Calendar.DAY_OF_YEAR, 1)
            return candidate.timeInMillis
        }

        for (offset in 0..7) {
            val check = candidate.clone() as Calendar
            check.add(Calendar.DAY_OF_YEAR, offset)
            if (check.get(Calendar.DAY_OF_WEEK) in alarm.repeatDays && check.timeInMillis > afterMillis) {
                return check.timeInMillis
            }
        }
        return candidate.timeInMillis
    }
}
