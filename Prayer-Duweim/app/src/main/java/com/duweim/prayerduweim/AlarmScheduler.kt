package com.duweim.prayerduweim

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.batoulapps.adhan.PrayerTimes
import java.util.Date

object AlarmScheduler {

    private const val FAJR_REQUEST = 100
    private const val DHUHR_REQUEST = 101
    private const val ASR_REQUEST = 102
    private const val MAGHRIB_REQUEST = 103
    private const val ISHA_REQUEST = 104

    fun setAlarms(context: Context, prayerTimes: PrayerTimes) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        cancelAll(context)

        setAlarm(
            context,
            alarmManager,
            prayerTimes.fajr,
            "الفجر",
            FAJR_REQUEST
        )

        setAlarm(
            context,
            alarmManager,
            prayerTimes.dhuhr,
            "الظهر",
            DHUHR_REQUEST
        )

        setAlarm(
            context,
            alarmManager,
            prayerTimes.asr,
            "العصر",
            ASR_REQUEST
        )

        setAlarm(
            context,
            alarmManager,
            prayerTimes.maghrib,
            "المغرب",
            MAGHRIB_REQUEST
        )

        setAlarm(
            context,
            alarmManager,
            prayerTimes.isha,
            "العشاء",
            ISHA_REQUEST
        )
    }

    private fun setAlarm(
        context: Context,
        alarmManager: AlarmManager,
        time: Date,
        prayerName: String,
        requestCode: Int
    ) {
        val intent = Intent(
            context,
            PrayerAlarmReceiver::class.java
        ).apply {
            putExtra("PRAYER_NAME", prayerName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time.time,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                time.time,
                pendingIntent
            )
        }
    }

    private fun cancelAll(context: Context) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(
            context,
            PrayerAlarmReceiver::class.java
        )

        val requestCodes = listOf(
            FAJR_REQUEST,
            DHUHR_REQUEST,
            ASR_REQUEST,
            MAGHRIB_REQUEST,
            ISHA_REQUEST
        )

        for (code in requestCodes) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                code,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)
        }
    }
}
