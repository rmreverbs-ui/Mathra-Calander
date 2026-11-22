package com.mathra.clockcalendar.data.holidays

import java.time.LocalDate

/**
 * Sri Lankan public holidays, Poya days and major festivals for 2026.
 *
 * SOURCE OF TRUTH: Government of Sri Lanka, Department of Government Printing —
 * Extraordinary Gazette No. 2438/22 (27.05.2025), "Public and Bank Holidays for
 * the Year 2026" (https://documents.gov.lk/view/extra-gazettes/2025/5/2438-22_E.pdf),
 * cross-checked against the Central Bank of Sri Lanka bank-holiday notice for 2026.
 *
 * IMPORTANT — DO NOT HARDCODE FUTURE YEARS FROM MEMORY:
 * Poya dates and several holiday dates change every year because they follow
 * the lunar calendar. This table must be refreshed from the annual government
 * gazette (published every December for the following year) before each new
 * year. Treat anything beyond 2026 in this file as a TODO, not as fact.
 */
object HolidayData {

    val HOLIDAYS_2026: List<Holiday> = listOf(
        Holiday(LocalDate.of(2026, 1, 1), "holiday_new_year_day", HolidayType.PUBLIC_HOLIDAY, "newyear"),
        Holiday(LocalDate.of(2026, 1, 3), "holiday_duruthu_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 1, 15), "holiday_thai_pongal", HolidayType.PUBLIC_HOLIDAY, "pongal"),
        Holiday(LocalDate.of(2026, 2, 1), "holiday_navam_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 2, 4), "holiday_independence_day", HolidayType.PUBLIC_HOLIDAY, "independence"),
        Holiday(LocalDate.of(2026, 2, 15), "holiday_maha_sivarathri", HolidayType.RELIGIOUS_OBSERVANCE),
        Holiday(LocalDate.of(2026, 3, 2), "holiday_medin_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 3, 21), "holiday_id_ul_fitr", HolidayType.PUBLIC_HOLIDAY, "eid"),
        Holiday(LocalDate.of(2026, 4, 1), "holiday_bak_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 4, 3), "holiday_good_friday", HolidayType.PUBLIC_HOLIDAY),
        Holiday(LocalDate.of(2026, 4, 13), "holiday_day_before_new_year", HolidayType.PUBLIC_HOLIDAY, "avurudu"),
        Holiday(LocalDate.of(2026, 4, 14), "holiday_sinhala_tamil_new_year", HolidayType.PUBLIC_HOLIDAY, "avurudu"),
        Holiday(LocalDate.of(2026, 5, 1), "holiday_vesak_poya", HolidayType.POYA_DAY, "vesak"),
        Holiday(LocalDate.of(2026, 5, 1), "holiday_may_day", HolidayType.PUBLIC_HOLIDAY),
        Holiday(LocalDate.of(2026, 5, 2), "holiday_day_after_vesak", HolidayType.PUBLIC_HOLIDAY, "vesak"),
        Holiday(LocalDate.of(2026, 5, 28), "holiday_id_ul_alha", HolidayType.PUBLIC_HOLIDAY, "eid"),
        Holiday(LocalDate.of(2026, 5, 30), "holiday_adhi_poson_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 6, 29), "holiday_poson_poya", HolidayType.POYA_DAY, "poson"),
        Holiday(LocalDate.of(2026, 7, 29), "holiday_esala_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 8, 26), "holiday_milad_un_nabi", HolidayType.PUBLIC_HOLIDAY),
        Holiday(LocalDate.of(2026, 8, 27), "holiday_nikini_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 9, 26), "holiday_binara_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 10, 25), "holiday_vap_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 11, 8), "holiday_deepavali", HolidayType.PUBLIC_HOLIDAY, "deepavali"),
        Holiday(LocalDate.of(2026, 11, 24), "holiday_il_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 12, 23), "holiday_unduvap_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2026, 12, 25), "holiday_christmas", HolidayType.PUBLIC_HOLIDAY, "christmas"),
    )

    /**
     * 2027: all 25 public and bank holidays of Gazette Extraordinary No. 2493/05 (16 June 2026,
     * Holidays Act No. 29 of 1971, Order under Section 4).
     * NOTE: the gazette PDF itself could not be opened while building this. The dates were taken from
     * published listings of that gazette (Wikipedia "2027 in Sri Lanka" citing it, officeholidays.com,
     * the Government Printing desk calendar listing, qppstudio.net) and cross-checked with the Central
     * Bank of Sri Lanka list (15 Jan, 22 Jan, 4 Feb). Sources agreed on all 25 dates and the count.
     * Please still compare against documents.gov.lk before publishing the app.
     */
    val HOLIDAYS_2027: List<Holiday> = listOf(
        Holiday(LocalDate.of(2027, 1, 15), "holiday_thai_pongal", HolidayType.PUBLIC_HOLIDAY, "pongal"),
        Holiday(LocalDate.of(2027, 1, 22), "holiday_duruthu_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 2, 4), "holiday_independence_day", HolidayType.PUBLIC_HOLIDAY, "independence"),
        Holiday(LocalDate.of(2027, 2, 20), "holiday_navam_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 3, 6), "holiday_maha_sivarathri", HolidayType.RELIGIOUS_OBSERVANCE),
        Holiday(LocalDate.of(2027, 3, 10), "holiday_id_ul_fitr", HolidayType.PUBLIC_HOLIDAY, "eid"),
        Holiday(LocalDate.of(2027, 3, 22), "holiday_medin_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 3, 26), "holiday_good_friday", HolidayType.PUBLIC_HOLIDAY),
        Holiday(LocalDate.of(2027, 4, 13), "holiday_day_before_new_year", HolidayType.PUBLIC_HOLIDAY, "avurudu"),
        Holiday(LocalDate.of(2027, 4, 14), "holiday_sinhala_tamil_new_year", HolidayType.PUBLIC_HOLIDAY, "avurudu"),
        Holiday(LocalDate.of(2027, 4, 20), "holiday_bak_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 5, 1), "holiday_may_day", HolidayType.PUBLIC_HOLIDAY),
        Holiday(LocalDate.of(2027, 5, 17), "holiday_id_ul_alha", HolidayType.PUBLIC_HOLIDAY, "eid"),
        Holiday(LocalDate.of(2027, 5, 19), "holiday_vesak_poya", HolidayType.POYA_DAY, "vesak"),
        Holiday(LocalDate.of(2027, 5, 20), "holiday_day_after_vesak", HolidayType.PUBLIC_HOLIDAY, "vesak"),
        Holiday(LocalDate.of(2027, 6, 18), "holiday_poson_poya", HolidayType.POYA_DAY, "poson"),
        Holiday(LocalDate.of(2027, 7, 18), "holiday_esala_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 8, 15), "holiday_milad_un_nabi", HolidayType.PUBLIC_HOLIDAY),
        Holiday(LocalDate.of(2027, 8, 16), "holiday_nikini_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 9, 15), "holiday_binara_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 10, 15), "holiday_vap_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 10, 28), "holiday_deepavali", HolidayType.PUBLIC_HOLIDAY, "deepavali"),
        Holiday(LocalDate.of(2027, 11, 13), "holiday_il_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 12, 13), "holiday_unduvap_poya", HolidayType.POYA_DAY),
        Holiday(LocalDate.of(2027, 12, 25), "holiday_christmas", HolidayType.PUBLIC_HOLIDAY, "christmas"),
    )

    /** Last year whose gazette is in this file. Add the next year here only once its gazette is published. */
    const val LAST_GAZETTED_YEAR = 2027

    /** False for years whose holiday gazette has not been published (and entered above) yet. */
    fun isGazetted(year: Int): Boolean = year in 2026..LAST_GAZETTED_YEAR

    fun forYear(year: Int): List<Holiday> = when (year) {
        2026 -> HOLIDAYS_2026
        2027 -> HOLIDAYS_2027
        else -> emptyList() // No verified data yet — surface an "update holiday data" prompt in UI instead of guessing
    }

    fun forDate(date: LocalDate): List<Holiday> = forYear(date.year).filter { it.date == date }

    fun poyaDays(year: Int): List<Holiday> = forYear(year).filter { it.type == HolidayType.POYA_DAY }

    fun nextUpcoming(from: LocalDate, limit: Int = 5): List<Holiday> =
        (forYear(from.year) + forYear(from.year + 1))
            .filter { !it.date.isBefore(from) }
            .sortedBy { it.date }
            .take(limit)
}
