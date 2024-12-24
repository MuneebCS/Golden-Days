package com.example.goldendays.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.goldendays.data.entities.Media
import kotlinx.coroutines.flow.Flow
@Dao
interface MediaDao {

    @Insert
    suspend fun insertMedia(media: Media)




    @Delete
    suspend fun deleteMedia(media: Media)

    @Query("SELECT * FROM media WHERE eventId = :eventId")
    fun getMediaByEventId(eventId: Int): Flow<List<Media>>


    @Query("SELECT * FROM media WHERE mediaId = :mediaId")
    fun getMedia(mediaId: Int): Flow<Media>
}
