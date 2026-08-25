package com.duweim.prayerduweim

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.util.Date

object PrayerTimesCalculator {

  private const val LATITUDE = 14.0
  private const val LONGITUDE = 32.0

  fun getPrayerTimes(date: Date = Date()): PrayerTimes {
    val coordinates = Coordinates(LATITUDE, LONGITUDE)
    val dateComponents = DateComponents.from(date)
    val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
    params.madhab = Madhab.HANAFI

    return PrayerTimes(coordinates, dateComponents, params)
  }

  fun getPrayerTimes(latitude: Double, longitude: Double, date: Date = Date()): PrayerTimes {
    val coordinates = Coordinates(latitude, longitude)
    val dateComponents = DateComponents.from(date)
    val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
    params.madhab = Madhab.HANAFI

    return PrayerTimes(coordinates, dateComponents, params)
  }
}