package com.example.goldendays.AppModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldendays.data.entities.Media
import com.example.goldendays.data.repository.MediaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val mediaRepo: MediaRepo
) : ViewModel() {

    private val _mediaList = MutableStateFlow<List<Media>>(emptyList())
    val mediaList: StateFlow<List<Media>> get() = _mediaList

    private val _media = MutableStateFlow<Media?>(null)
    val media: StateFlow<Media?> get() = _media

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    // Loads a list of media for a specific event
    fun loadMediaForEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                mediaRepo.getMediaByEventId(eventId).collect { media ->
                    _mediaList.value = media
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error loading media for event: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Loads a single media by its ID
    fun loadMedia(mediaId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                mediaRepo.getMedia(mediaId).collect { media ->
                    _media.value = media
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error loading media: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Add media from the gallery and refresh the media list
    fun addMediaFromGallery(eventId: Int, mediaUri: ByteArray, mediaType: String) {
        val newMedia = Media(
            uri = mediaUri,
            type = mediaType,
            eventId = eventId
        )
        viewModelScope.launch {
            try {
                mediaRepo.insertMedia(newMedia)
                loadMediaForEvent(eventId) // Refresh media list after adding new media
            } catch (e: Exception) {
                _error.value = "Error adding media: ${e.localizedMessage}"
            }
        }
    }

    fun deleteMedia(media: Media) {
        viewModelScope.launch {
            try {
                mediaRepo.deleteMedia(media)
                loadMediaForEvent(media.eventId)
            } catch (e: Exception) {
                _error.value = "Error deleting media: ${e.localizedMessage}"
            }
        }
    }
}
