package com.mathra.clockcalendar.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mathra.clockcalendar.MainActivity
import com.mathra.clockcalendar.MathraApp
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.data.local.ReminderEntity
import com.mathra.clockcalendar.data.settings.SettingsRepository
import com.mathra.clockcalendar.util.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Fired by AlarmManager when a reminder is due: shows the notification, then schedules the next repeat. */
class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
        private const val NOTIFICATION_ID_BASE = 100_000
    }

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(EXTRA_REMINDER_ID, -1L)
        if (id < 0) return

        val appContext = context.applicationContext
        val app = appContext as MathraApp
        val pending = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reminder = app.database.reminderDao().getById(id) ?: return@launch
                if (reminder.isCompleted) return@launch

                val settings = SettingsRepository(appContext)
                if (settings.notificationsEnabled.first()) {
                    show(appContext, reminder, settings.vibrationEnabled.first())
                }
                // Repeating reminders: schedule the next one (skip anything within the next minute
                // so an early-firing alarm cannot notify twice).
                ReminderScheduler(appContext).schedule(reminder, System.currentTimeMillis() + 60_000)
            } finally {
                pending.finish()
            }
        }
    }

    private fun show(context: Context, reminder: ReminderEntity, vibrate: Boolean) {
        val contentIntent = PendingIntent.getActivity(
            context, reminder.id.toInt(),
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, MathraApp.CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(reminder.title)
            .setContentText(reminder.note.ifBlank { context.getString(R.string.reminder_due) })
            .setStyle(NotificationCompat.BigTextStyle().bigText(reminder.note.ifBlank { context.getString(R.string.reminder_due) }))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
        if (vibrate) builder.setVibrate(longArrayOf(0, 300, 200, 300))

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            manager.notify(NOTIFICATION_ID_BASE + reminder.id.toInt(), builder.build())
        } catch (_: SecurityException) {
            // Notification permission was revoked; nothing else we can do here.
        }
    }
}
