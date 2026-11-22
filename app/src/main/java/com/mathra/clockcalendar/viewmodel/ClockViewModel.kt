package com.mathra.clockcalendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathra.clockcalendar.util.DateUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

/** Ticks the real system clock every second. Never synthesizes a fake time. */
class ClockViewModel : ViewModel() {
    private val _time = MutableStateFlow(DateUtils.now())
    val time: StateFlow<LocalTime> = _time

    private val _date = MutableStateFlow(DateUtils.today())
    val date: StateFlow<LocalDate> = _date

    init {
        viewModelScope.launch {
            while (true) {
                _time.value = DateUtils.now()
                _date.value = DateUtils.today()
                delay(1000)
            }
        }
    }
}
