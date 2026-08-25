package com.duweim.prayerduweim

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PrayerAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "prayer_alarm_channel"
        const val NOTIFICATION_ID = 1001
    }

    override fun onReceive(context: Context, intent: Intent) {

        val prayerName =
            intent.getStringExtra("PRAYER_NAME") ?: "الصلاة"

        createNotificationChannel(context)

        showNotification(context, prayerName)

        playAdhan(context)
    }

    private fun showNotification(
        context: Context,
        prayerName: String
    ) {

        val openIntent = Intent(
            context,
            MainActivity::class.java
        ).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE
        )

        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(
                    android.R.drawable.ic_lock_idle_alarm
                )
                .setContentTitle("حان موعد الصلاة")
                .setContentText(
                    "حان الآن موعد $prayerName"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setCategory(
                    NotificationCompat.CATEGORY_ALARM
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            context.checkSelfPermission(
                android.Manifest.permission.POST_NOTIFICATIONS
            ) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat
                .from(context)
                .notify(
                    NOTIFICATION_ID,
                    notification
                )
        }
    }

    private fun createNotificationChannel(
        context: Context
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "تنبيهات الصلاة",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    "إشعارات دخول أوقات الصلاة"
            }

            val manager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    private fun playAdhan(context: Context) {

        /*
         * سيتم تشغيل الأذان إذا وضعنا ملف:
         *
         * app/src/main/res/raw/adhan.mp3
         *
         * إذا لم يكن الملف موجودًا فلن يتعطل التطبيق.
         */

        try {

            val resourceId =
                context.resources.getIdentifier(
                    "adhan",
                    "raw",
                    context.packageName
                )

            if (resourceId == 0) {
                return
            }

            val mediaPlayer =
                MediaPlayer.create(
                    context,
                    resourceId
                )

            mediaPlayer?.start()

            mediaPlayer?.setOnCompletionListener {
                it.release()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
