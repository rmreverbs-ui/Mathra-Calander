package com.mathra.clockcalendar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mathra.clockcalendar.MathraApp
import com.mathra.clockcalendar.data.local.AlarmEntity
import com.mathra.clockcalendar.data.repository.AlarmRepository
import com.mathra.clockcalendar.util.AlarmScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AlarmRepository(MathraApp.instance.database.alarmDao())
    private val scheduler = AlarmScheduler(application)

    val alarms: StateFlow<List<AlarmEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addAlarm(hour: Int, minute: Int, label: String, repeatDays: Set<Int> = emptySet()) {
        viewModelScope.launch {
            val id = repository.add(AlarmEntity(hour = hour, minute = minute, label = label, repeatDays = repeatDays))
            val saved = AlarmEntity(id = id, hour = hour, minute = minute, label = label, repeatDays = repeatDays)
            scheduler.schedule(saved)
        }
    }

    fun toggleEnabled(alarm: AlarmEntity) {
        viewModelScope.launch {
            val updated = alarm.copy(isEnabled = !alarm.isEnabled)
            repository.update(updated)
            if (updated.isEnabled) scheduler.schedule(updated) else scheduler.cancel(updated)
        }
    }

    fun delete(alarm: AlarmEntity) {
        viewModelScope.launch {
            scheduler.cancel(alarm)
            repository.delete(alarm)
        }
    }
}
