package com.mathra.clockcalendar.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mathra.clockcalendar.data.local.ReminderEntity
import com.mathra.clockcalendar.receiver.ReminderReceiver

/** Schedules the notification for a reminder through AlarmManager. */
class ReminderScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /** Schedules the next occurrence; cancels instead if completed or already past. */
    fun schedule(reminder: ReminderEntity, afterMillis: Long = System.currentTimeMillis()) {
        if (reminder.isCompleted) { cancel(reminder); return }
        val trigger = ReminderTime.nextTrigger(reminder, afterMillis)
        if (trigger == null) { cancel(reminder); return }
        alarmManager.setBestEffort(context, trigger, pendingIntentFor(reminder.id))
    }

    fun cancel(reminder: ReminderEntity) {
        alarmManager.cancel(pendingIntentFor(reminder.id))
    }

    private fun pendingIntentFor(id: Long): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_REMINDER_ID, id)
        }
        return PendingIntent.getBroadcast(
            context, id.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
