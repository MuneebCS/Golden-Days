package com.example.goldendays.data.repository

import androidx.lifecycle.LiveData
import com.example.goldendays.data.database.MediaDao
import com.example.goldendays.data.entities.Media
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaRepo @Inject constructor(private val mediaDao: MediaDao){
    suspend fun insertMedia(media: Media) = mediaDao.insertMedia(media)
    suspend fun deleteMedia(media: Media) = mediaDao.deleteMedia(media)
    fun getMediaByEventId(eventId: Int): Flow<List<Media>> {
        return mediaDao.getMediaByEventId(eventId = eventId)
    }
    fun getMedia(mediaId: Int): Flow<Media> {
        return mediaDao.getMedia(mediaId = mediaId)
    }

}
