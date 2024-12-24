import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import java.time.format.TextStyle

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    navController: NavController,
) {
    val eventViewModel: EventViewModel = hiltViewModel()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var datePickerVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create New Event",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = name,
            onValueChange = {
                name = it
                if (errorMessage != null) {
                    errorMessage = null
                }
            },
            label = "Event Name",
        )

        CustomTextField(
            value = description,
            onValueChange = {
                description = it
                // Clear error message when the user starts typing in description
                if (errorMessage != null) {
                    errorMessage = null
                }
            },
            label = "Event Description",
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

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (name.isEmpty() || description.isEmpty()) {
                errorMessage = "Incomplete Information, Try Again!"
            } else {
                errorMessage = null
                val epochMillis = selectedDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
                val event = Event(
                    name = name,
                    date = epochMillis,
                    description = description
                )
                eventViewModel.addEvent(event)
                navController.popBackStack()
            }
        }) {
            Text("Save Event")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
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

