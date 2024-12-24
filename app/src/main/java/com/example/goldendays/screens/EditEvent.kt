package com.example.goldendays.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.goldendays.AppModule.EventViewModel
import com.example.goldendays.data.entities.Event
import com.example.goldendays.screens.components.CustomTextField
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    navController: NavController,
    eventId: Int,
    eventViewModel: EventViewModel = hiltViewModel()
) {

    val event by eventViewModel.event.collectAsState(initial = null)

    LaunchedEffect(eventId) {
        eventViewModel.getEventById(eventId)
    }

    if (event == null) {
        Text("Event not found", style = MaterialTheme.typography.bodyLarge)
        return
    }


    var name by remember { mutableStateOf(event?.name ?: "") }
    var description by remember { mutableStateOf(event?.description ?: "") }
    var selectedDate by remember {
        mutableStateOf(
            event?.date?.let { millis ->
                LocalDate.ofEpochDay(millis / 86400000)
            } ?: LocalDate.now()
        )
    }
    var datePickerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTextField(
            value = name,
            onValueChange = { name = it },
            label = "Event Name",
        )
        CustomTextField(
            value = description,
            onValueChange = { description = it },
            label = "Event Name",
        )


        Text("Selected date: ${selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}", style = MaterialTheme.typography.displaySmall)
        Button(onClick = { datePickerVisible = true }) {
            Text("Pick Date")
        }

        if (datePickerVisible) {
            DatePickerDialog(
                initialDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    datePickerVisible = false
                },
                onDismissRequest = { datePickerVisible = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            try {

                val epochMillis = selectedDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()


                val updatedEvent = event?.copy(
                    name = name,
                    date = epochMillis,
                    description = description
                )

                if (updatedEvent != null) {
                    eventViewModel.updateEvent(updatedEvent)
                    navController.popBackStack() // Navigate back after update
                }
            } catch (e: Exception) {
                Log.e("EditEventScreen", "Date format error", e)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Update Event")
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    val customTypography = MaterialTheme.typography.copy(
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp)
    )

    MaterialTheme(typography = customTypography) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Select a Date") },
            text = {
                DatePicker(state = state)
            },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDateMillis = state.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val selectedDate = LocalDate.ofEpochDay(selectedDateMillis / 86400000)
                        onDateSelected(selectedDate)
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            }
        )
    }
}
