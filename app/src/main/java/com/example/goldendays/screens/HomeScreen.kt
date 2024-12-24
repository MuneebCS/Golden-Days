package com.example.goldendays.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.goldendays.AppModule.EventViewModel
import com.example.goldendays.R
import com.example.goldendays.data.entities.Event
import com.example.goldendays.screens.components.EventCard
import com.example.goldendays.screens.components.SearchHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val eventViewModel: EventViewModel = hiltViewModel()
    val events by eventViewModel.events.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Golden Days",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_event_screen") },
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            SearchHeader(
                modifier = Modifier.fillMaxWidth(),
                onSearchEventChanged = { event ->
                    eventViewModel.searchEvents(event)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                if (events.isEmpty()) {
                    EmptyEventMessage()
                } else {
                    EventList(
                        events = events,
                        onEventClick = { event ->
                            navController.navigate("event_detail_screen/${event.id}")
                        },
                        onDeleteClick = { event ->
                            eventViewModel.deleteEvent(event)
                        },
                        onEditClick = { event ->
                            navController.navigate("edit_event_screen/${event.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EventList(
    events: List<Event>,
    onEventClick: (Event) -> Unit,
    onDeleteClick: (Event) -> Unit,
    onEditClick: (Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {
        items(events) { event ->
            EventCard(
                event = event,
                onClick = { onEventClick(event) },
                onDeleteClick = { onDeleteClick(event) },
                onEditClick = { onEditClick(event) },
                backgroundPainter = painterResource(id = R.drawable.bg2)
            )
        }
    }
}

@Composable
fun EmptyEventMessage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "No events found",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
