package com.example.goldendays.data.repository

import androidx.lifecycle.LiveData
import com.example.goldendays.data.database.EventDao
import com.example.goldendays.data.entities.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepo @Inject constructor(private val eventDao: EventDao){
    fun getAllEvents(): Flow<List<Event>> = eventDao.getEvent()
    suspend fun insertEvent(event: Event) = eventDao.insertEvent(event)
    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event)
    suspend fun deleteEvent(event: Event) = eventDao.deleteEvent(event)
    suspend fun searchEvents(event: String): Flow<List<Event>> = eventDao.searchEvents(event)
    fun getEventById(eventId: Int): Flow<Event> {
        return eventDao.getEventById(eventId)
    }

}
