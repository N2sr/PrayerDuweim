package com.duweim.prayerduweim

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

  private lateinit var fajrText: TextView
  private lateinit var dhuhrText: TextView
  private lateinit var asrText: TextView
  private lateinit var maghribText: TextView
  private lateinit var ishaText: TextView
  private lateinit var updateButton: Button

  private val requestPermissionLauncher =
  registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    isGranted ->
    if (isGranted) {
      setupAlarms()
    } else {
      Toast.makeText(this, "لن تظهر إشعارات الصلاة", Toast.LENGTH_LONG).show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    fajrText = findViewById(R.id.fajrText)
    dhuhrText = findViewById(R.id.dhuhrText)
    asrText = findViewById(R.id.asrText)
    maghribText = findViewById(R.id.maghribText)
    ishaText = findViewById(R.id.ishaText)
    updateButton = findViewById(R.id.updateButton)

    displayPrayerTimes()
    checkAndRequestPermissions()

    updateButton.setOnClickListener {
      displayPrayerTimes()
      setupAlarms()
      Toast.makeText(this, "تم تحديث الأوقات", Toast.LENGTH_SHORT).show()
    }
  }

  private fun displayPrayerTimes() {
    val prayerTimes = PrayerTimesCalculator.getPrayerTimes()
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

    fajrText.text = "الفجر: ${sdf.format(prayerTimes.fajr)}"
    dhuhrText.text = "الظهر: ${sdf.format(prayerTimes.dhuhr)}"
    asrText.text = "العصر: ${sdf.format(prayerTimes.asr)}"
    maghribText.text = "المغرب: ${sdf.format(prayerTimes.maghrib)}"
    ishaText.text = "العشاء: ${sdf.format(prayerTimes.isha)}"
  }

  private fun checkAndRequestPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      when {
        ContextCompat.checkSelfPermission(
          this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED -> {
          checkExactAlarmPermission()
        }
        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
          requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else -> {
          requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
      }
    } else {
      checkExactAlarmPermission()
    }
  }

  private fun checkExactAlarmPermission() {
    val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      if (!alarmManager.canScheduleExactAlarms()) {
        Toast.makeText(
          this,
          "يرجى تفعيل صلاحية المنبه الدقيق من الإعدادات",
          Toast.LENGTH_LONG
        ).show()
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
          data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
      } else {
        setupAlarms()
      }
    } else {
      setupAlarms()
    }
  }

  private fun setupAlarms() {
    val prayerTimes = PrayerTimesCalculator.getPrayerTimes()
    AlarmScheduler.setAlarms(this, prayerTimes)
    Toast.makeText(this, "تم ضبط منبهات الصلاة", Toast.LENGTH_SHORT).show()
  }
}