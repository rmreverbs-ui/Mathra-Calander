package com.mathra.clockcalendar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mathra.clockcalendar.MathraApp
import com.mathra.clockcalendar.data.local.ReminderEntity
import com.mathra.clockcalendar.data.local.ReminderRepeat
import com.mathra.clockcalendar.data.repository.ReminderRepository
import com.mathra.clockcalendar.util.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RemindersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReminderRepository(MathraApp.instance.database.reminderDao())
    private val scheduler = ReminderScheduler(application)

    val reminders: StateFlow<List<ReminderEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addReminder(
        title: String,
        epochMillis: Long,
        note: String = "",
        repeat: ReminderRepeat = ReminderRepeat.NONE
    ) {
        viewModelScope.launch {
            val draft = ReminderEntity(title = title, note = note, epochMillis = epochMillis, repeat = repeat)
            val id = repository.add(draft)
            scheduler.schedule(draft.copy(id = id))
        }
    }

    fun toggleCompleted(reminder: ReminderEntity) {
        viewModelScope.launch {
            val updated = reminder.copy(isCompleted = !reminder.isCompleted)
            repository.update(updated)
            scheduler.schedule(updated) // cancels when completed, re-arms when un-completed
        }
    }

    fun delete(reminder: ReminderEntity) {
        viewModelScope.launch {
            scheduler.cancel(reminder)
            repository.delete(reminder)
        }
    }
}
