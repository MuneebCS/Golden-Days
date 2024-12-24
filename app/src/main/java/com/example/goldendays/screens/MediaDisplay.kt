package com.example.goldendays.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.goldendays.AppModule.MediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDisplayScreen(navController: NavController, mediaId: Int) {
    val mediaViewModel: MediaViewModel = hiltViewModel()

    // Observe the media state from ViewModel
    val media = mediaViewModel.media.collectAsState().value
    val isLoading = mediaViewModel.isLoading.collectAsState().value
    val error = mediaViewModel.error.collectAsState().value

    // Load media if not already loaded
    if (media == null || media.mediaId != mediaId) {
        mediaViewModel.loadMedia(mediaId)
    }

    // Check if loading is in progress
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Loading media...", modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    // Show error message if loading failed
    if (error != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Error loading media: $error", modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    if (media != null && media.mediaId == mediaId) {
        val imageBitmap = remember(media.uri) {
            BitmapFactory.decodeByteArray(media.uri, 0, media.uri.size)
        }

        Scaffold(
            topBar = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }

            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        mediaViewModel.deleteMedia(media)
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Media"
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                imageBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Full Screen Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: run {
                    Text(
                        text = "Error Displaying Images",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
