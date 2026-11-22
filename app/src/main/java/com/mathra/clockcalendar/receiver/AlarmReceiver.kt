package com.mathra.clockcalendar.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.mathra.clockcalendar.MainActivity
import com.mathra.clockcalendar.MathraApp
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.data.settings.SettingsRepository
import com.mathra.clockcalendar.util.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Fired by AlarmManager at the scheduled time. Rings (repeating sound until dismissed),
 * then either schedules the next occurrence (repeating alarms) or switches a one-time alarm off.
 */
class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_ALARM_ID = "extra_alarm_id"
        const val EXTRA_LABEL = "extra_label"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, 0L)
        val fallbackLabel = intent.getStringExtra(EXTRA_LABEL) ?: context.getString(R.string.card_alarm)
        val appContext = context.applicationContext
        val app = appContext as MathraApp
        val pending = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = app.database.alarmDao()
                val alarm = dao.getById(alarmId)
                // Deleted or switched off since it was scheduled: stay silent.
                if (alarm != null && !alarm.isEnabled) return@launch

                val vibrate = SettingsRepository(appContext).vibrationEnabled.first()
                show(appContext, alarmId, alarm?.label?.ifBlank { null } ?: fallbackLabel, vibrate)

                if (alarm != null) {
                    if (alarm.repeatDays.isEmpty()) {
                        dao.update(alarm.copy(isEnabled = false)) // one-time alarm is done
                    } else {
                        AlarmScheduler(appContext).schedule(alarm, System.currentTimeMillis() + 60_000)
                    }
                }
            } finally {
                pending.finish()
            }
        }
    }

    private fun show(context: Context, alarmId: Long, label: String, vibrate: Boolean) {
        val contentIntent = PendingIntent.getActivity(
            context, alarmId.toInt(),
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val builder = NotificationCompat.Builder(context, MathraApp.CHANNEL_ALARMS)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(label)
            .setContentText(context.getString(R.string.tagline))
            .setSound(sound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
        if (vibrate) builder.setVibrate(longArrayOf(0, 500, 300, 500, 300, 500))

        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_INSISTENT // keep ringing until dismissed

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            manager.notify(alarmId.toInt(), notification)
        } catch (_: SecurityException) {
        }
    }
}
