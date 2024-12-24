// File: screens/components/SearchHeader.kt

package com.example.goldendays.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchHeader(
    modifier: Modifier = Modifier,
    onSearchEventChanged: (String) -> Unit
) {
    var event by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = event,
                onValueChange = {
                    event = it
                    onSearchEventChanged(it.text)
                },
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp),

                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    if (event.text.isEmpty()) {
                        Text(
                            text = "Search Events...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}
