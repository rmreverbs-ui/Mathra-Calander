package com.mathra.clockcalendar.data.repository

import com.mathra.clockcalendar.data.local.ReminderDao
import com.mathra.clockcalendar.data.local.ReminderEntity
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val dao: ReminderDao) {
    fun observeAll(): Flow<List<ReminderEntity>> = dao.observeAll()
    fun observeUpcoming(): Flow<List<ReminderEntity>> = dao.observeUpcoming()
    suspend fun add(reminder: ReminderEntity): Long = dao.insert(reminder)
    suspend fun update(reminder: ReminderEntity) = dao.update(reminder)
    suspend fun delete(reminder: ReminderEntity) = dao.delete(reminder)
}
