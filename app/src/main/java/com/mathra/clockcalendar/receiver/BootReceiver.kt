package com.mathra.clockcalendar.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mathra.clockcalendar.MathraApp
import com.mathra.clockcalendar.util.AlarmScheduler
import com.mathra.clockcalendar.util.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * AlarmManager entries are wiped by a reboot, an app update and (for wall-clock times) a time-zone
 * change, so every enabled alarm and every pending reminder is scheduled again here.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val handled = setOf(
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_TIMEZONE_CHANGED
        )
        if (intent.action !in handled) return

        val appContext = context.applicationContext
        val app = appContext as MathraApp
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val alarmScheduler = AlarmScheduler(appContext)
                app.database.alarmDao().observeAll().first()
                    .filter { it.isEnabled }
                    .forEach { alarmScheduler.schedule(it) }

                val reminderScheduler = ReminderScheduler(appContext)
                app.database.reminderDao().observeUpcoming().first()
                    .forEach { reminderScheduler.schedule(it) } // past one-time reminders are skipped
            } finally {
                pendingResult.finish()
            }
        }
    }
}
