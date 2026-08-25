package com.duweim.prayerduweim

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.util.Date

object PrayerTimesCalculator {

    // إحداثيات مدينة الدويم - السودان
    // يمكنك تغييرها لاحقاً حسب المدينة المطلوبة
    private const val LATITUDE = 14.0    // درجة العرض
    private const val LONGITUDE = 32.0   // درجة الطول

    /**
     * حساب مواقيت الصلاة لتاريخ معين (التاريخ الحالي افتراضياً)
     */
    fun getPrayerTimes(date: Date = Date()): PrayerTimes {
        // تحويل الإحداثيات
        val coordinates = Coordinates(LATITUDE, LONGITUDE)

        // تحويل التاريخ إلى المكونات المطلوبة
        val dateComponents = DateComponents.from(date)

        // استخدام طريقة حساب رابطة العالم الإسلامي
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        // المذهب الحنفي (شائع في السودان)
        params.madhab = Madhab.HANAFI

        // حساب الأوقات وإرجاعها
        return PrayerTimes(coordinates, dateComponents, params)
    }

    /**
     * لتغيير الإحداثيات (عندما نضيف شاشة الإعدادات لاحقاً)
     */
    fun getPrayerTimes(latitude: Double, longitude: Double, date: Date = Date()): PrayerTimes {
        val coordinates = Coordinates(latitude, longitude)
        val dateComponents = DateComponents.from(date)
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        params.madhab = Madhab.HANAFI

        return PrayerTimes(coordinates, dateComponents, params)
    }
}