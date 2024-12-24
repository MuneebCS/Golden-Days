package com.example.goldendays.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    TextField(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Normal,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        ),
        singleLine = true,
        maxLines = 2
    )
}
