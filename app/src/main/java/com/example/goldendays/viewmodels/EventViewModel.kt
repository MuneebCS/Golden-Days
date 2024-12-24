package com.example.goldendays.AppModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldendays.data.entities.Event
import com.example.goldendays.data.repository.EventRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepo: EventRepo
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    private val _event = MutableStateFlow<Event?>(null)
    private val _error = MutableStateFlow<String?>(null)

    val events: StateFlow<List<Event>> = _events
    val event: StateFlow<Event?> = _event
    val error: StateFlow<String?> = _error

    init {
        // Fetch all events and handle errors
        viewModelScope.launch {
            try {
                eventRepo.getAllEvents().collect { eventList ->
                    _events.value = eventList
                }
            } catch (e: Exception) {
                _error.value = "Error fetching events: ${e.message}"
            }
        }
    }

    // Add a new event and handle errors
    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventRepo.insertEvent(event)
            } catch (e: Exception) {
                _error.value = "Error adding event: ${e.message}"
            }
        }
    }

    // Update an existing event and handle errors
    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventRepo.updateEvent(event)
            } catch (e: Exception) {
                _error.value = "Error updating event: ${e.message}"
            }
        }
    }

    // Delete an event and handle errors
    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventRepo.deleteEvent(event)
            } catch (e: Exception) {
                _error.value = "Error deleting event: ${e.message}"
            }
        }
    }

    // Get a single event by its ID and handle errors
    fun getEventById(eventId: Int) {
        viewModelScope.launch {
            try {
                eventRepo.getEventById(eventId).collect { event ->
                    _event.value = event
                }
            } catch (e: Exception) {
                _error.value = "Error fetching event by ID: ${e.message}"
            }
        }
    }

    fun searchEvents(event: String) {
        viewModelScope.launch {
            try {
                eventRepo.searchEvents(event).collect { eventList ->
                    _events.value = eventList
                }
            } catch (e: Exception) {
                _error.value = "Error searching events: ${e.message}"
            }
        }
    }

}
