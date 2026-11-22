package com.mathra.clockcalendar.data.holidays

import java.time.LocalDate

enum class HolidayType { PUBLIC_HOLIDAY, POYA_DAY, RELIGIOUS_OBSERVANCE, CULTURAL_FESTIVAL }

/**
 * A single calendar-fixed holiday/festival entry.
 * [nameRes] / [nameResSi] point at string resources so the UI can show
 * the correct language without duplicating text here.
 */
data class Holiday(
    val date: LocalDate,
    val nameRes: String,     // string resource name, resolved via context at display time
    val type: HolidayType,
    val festivalKey: String? = null // used to look up a themed festival screen (see FestivalThemes)
)
