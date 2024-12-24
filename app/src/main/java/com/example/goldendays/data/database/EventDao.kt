package com.example.goldendays.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.goldendays.data.entities.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert
   suspend fun insertEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("Select * from events")
    fun getEvent() : Flow<List<Event>>

    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventById(eventId: Int): Flow<Event>

    @Query("SELECT * FROM events WHERE name LIKE '%' || :eventName || '%'")
    fun searchEvents(eventName: String): Flow<List<Event>>

}