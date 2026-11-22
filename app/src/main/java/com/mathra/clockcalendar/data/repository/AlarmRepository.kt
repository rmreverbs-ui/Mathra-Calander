package com.mathra.clockcalendar.data.repository

import com.mathra.clockcalendar.data.local.AlarmDao
import com.mathra.clockcalendar.data.local.AlarmEntity
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val dao: AlarmDao) {
    fun observeAll(): Flow<List<AlarmEntity>> = dao.observeAll()
    suspend fun add(alarm: AlarmEntity): Long = dao.insert(alarm)
    suspend fun update(alarm: AlarmEntity) = dao.update(alarm)
    suspend fun delete(alarm: AlarmEntity) = dao.delete(alarm)
}
