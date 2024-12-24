package com.example.goldendays.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EventDetailHeader(name:String, description:String) {
    Column{
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text =description, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))
    }

}