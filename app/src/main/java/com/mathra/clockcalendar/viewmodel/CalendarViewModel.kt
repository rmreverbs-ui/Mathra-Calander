package com.mathra.clockcalendar.viewmodel

import androidx.lifecycle.ViewModel
import com.mathra.clockcalendar.data.holidays.Holiday
import com.mathra.clockcalendar.data.holidays.HolidayData
import com.mathra.clockcalendar.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel : ViewModel() {
    private val _visibleMonth = MutableStateFlow(YearMonth.now(DateUtils.SRI_LANKA_ZONE))
    val visibleMonth: StateFlow<YearMonth> = _visibleMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtils.today())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    fun nextMonth() { _visibleMonth.value = _visibleMonth.value.plusMonths(1) }
    fun previousMonth() { _visibleMonth.value = _visibleMonth.value.minusMonths(1) }
    fun goToToday() { _visibleMonth.value = YearMonth.now(DateUtils.SRI_LANKA_ZONE); _selectedDate.value = DateUtils.today() }
    fun selectDate(date: LocalDate) { _selectedDate.value = date }

    fun holidaysFor(date: LocalDate): List<Holiday> = HolidayData.forDate(date)
    fun gridFor(month: YearMonth): List<LocalDate> = DateUtils.monthGrid(month.year, month.monthValue)
}
