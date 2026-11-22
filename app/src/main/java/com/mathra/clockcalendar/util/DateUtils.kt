package com.mathra.clockcalendar.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import java.util.Locale
import kotlin.math.abs

/**
 * All date/time logic for the app flows through here, on top of java.time
 * (java.time is desugared automatically on minSdk 26+, no fake clocks).
 */
object DateUtils {

    val SRI_LANKA_ZONE: ZoneId = ZoneId.of("Asia/Colombo")

    fun today(): LocalDate = LocalDate.now(SRI_LANKA_ZONE)
    fun now(): LocalTime = LocalTime.now(SRI_LANKA_ZONE)

    fun formatTime12(time: LocalTime, locale: Locale = Locale.ENGLISH): String = time.format(DateTimeFormatter.ofPattern("hh:mm:ss a", locale))
    fun formatTime24(time: LocalTime): String = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

    fun formatFullDate(date: LocalDate, locale: Locale = Locale.ENGLISH): String =
        date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", locale))

    fun daysBetween(from: LocalDate, to: LocalDate): Long = abs(java.time.temporal.ChronoUnit.DAYS.between(from, to))

    /** Returns the weekday header labels (Sun..Sat) for calendar grids. */
    fun weekdayLabels(locale: Locale = Locale.ENGLISH): List<String> =
        listOf(
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
        ).map { it.getDisplayName(TextStyle.SHORT, locale) }

    /** Builds a 6x7 grid of dates for the given month, including leading/trailing days from adjacent months. */
    fun monthGrid(year: Int, month: Int): List<LocalDate> {
        val first = LocalDate.of(year, month, 1)
        val firstWeekdayIndex = first.dayOfWeek.value % 7 // Sunday=0
        val start = first.minusDays(firstWeekdayIndex.toLong())
        return (0 until 42).map { start.plusDays(it.toLong()) }
    }

    fun isLeapYear(year: Int): Boolean = java.time.Year.isLeap(year.toLong())
}

/** The language the UI is currently shown in (English or Sinhala), for month / weekday names. */
@Composable
fun appLocale(): Locale = LocalConfiguration.current.locales[0] ?: Locale.ENGLISH
