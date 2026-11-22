package com.mathra.clockcalendar

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.mathra.clockcalendar.data.local.AppDatabase

/**
 * Application class. Initializes the Room database singleton and
 * registers the notification channels used by alarms and reminders.
 */
class MathraApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    companion object {
        const val CHANNEL_ALARMS = "mathra_alarms"
        const val CHANNEL_REMINDERS = "mathra_reminders"
        lateinit var instance: MathraApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ALARMS,
                    "Mathra Alarms",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Alarm notifications for Mathra Clock and Calendar"
                    setSound(
                        android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM),
                        android.media.AudioAttributes.Builder()
                            .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    enableVibration(true)
                }
            )
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_REMINDERS,
                    "Mathra Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Reminder and event notifications" }
            )
        }
    }
}
