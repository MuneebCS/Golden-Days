package com.example.goldendays.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.goldendays.AppModule.EventViewModel
import com.example.goldendays.AppModule.MediaViewModel
import com.example.goldendays.data.entities.Event
import com.example.goldendays.data.entities.Media
import com.example.goldendays.screens.components.EventDetailHeader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun EventDetailScreen(
    navController: NavController,
    eventId: Int
) {
    val context = LocalContext.current
    val eventViewModel: EventViewModel = hiltViewModel()
    val mediaViewModel: MediaViewModel = hiltViewModel()

    val event by eventViewModel.event.collectAsState(initial = null)
    val mediaList by mediaViewModel.mediaList.collectAsState(initial = emptyList())
    val isMediaLoading by mediaViewModel.isLoading.collectAsState()

    LaunchedEffect(eventId) {
        eventViewModel.getEventById(eventId)
        mediaViewModel.loadMediaForEvent(eventId)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uriList ->
            uriList.forEach { uri ->
                val type = context.contentResolver.getType(uri)
                val byteArray = getMediaByteArray(uri, context)

                byteArray?.let {
                    val mediaType = when {
                        type?.startsWith("image") == true -> "image"
                        type?.startsWith("video") == true -> "video"
                        else -> null
                    }

                    mediaType?.let { validType ->
                        val media = Media(eventId = eventId, uri = it, type = validType)
                        mediaViewModel.addMediaFromGallery(
                            media.eventId,
                            media.uri,
                            media.type
                        )
                    }
                }
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
                        )
                    )
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Media")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            if (event == null) {
                LoadingMessage("Loading event details...")
            } else {
                EventDetailContent(event = event!!, mediaList = mediaList, isMediaLoading, navController = navController)
            }
        }
    }
}

fun getMediaByteArray(uri: Uri, context: Context): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { it.readBytes() }
    } catch (e: Exception) {
        Log.e("EventDetailScreen", "Error converting URI to byte array", e)
        null
    }
}

@Composable
fun EventDetailContent(event: Event, mediaList: List<Media>, isLoading: Boolean, navController: NavController) {
    Column(modifier = Modifier.padding(10.dp)) {
        EventDetailHeader(event.name, event.description)
        if (isLoading) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        } else {
            if (mediaList.isEmpty()) {
                LoadingMessage("No media available.")
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(mediaList.size) { index ->
                        val media = mediaList[index]
                        MediaItem(navController = navController, media = media)
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItem(navController: NavController, media: Media) {
    val context = LocalContext.current
    val isVideo = media.type == "video"

    val imageBitmap = remember(media.uri) {
        if (isVideo) {
            extractVideoThumbnailFromByteArray(media.uri, context)
        } else {
            BitmapFactory.decodeByteArray(media.uri, 0, media.uri.size)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                if (isVideo) {
                    navController.navigate("media_playback/${media.mediaId}")
                } else {
                    navController.navigate("media_display/${media.mediaId}")
                }
            }
    ) {
        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = if (isVideo) "Video Thumbnail" else "Image",
            )

            if (isVideo) {
                IconButton(
                    onClick = { navController.navigate("media_playback/${media.mediaId}") },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), shape = CircleShape)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play Video",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } ?: run {
            Text(
                "Error loading media",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


fun extractVideoThumbnailFromByteArray(byteArray: ByteArray, context: Context): Bitmap? {
    return try {
        val tempFile = File.createTempFile("temp_video", ".mp4", context.cacheDir)
        FileOutputStream(tempFile).use { outputStream ->
            outputStream.write(byteArray)
        }

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(tempFile.absolutePath)

        val bitmap = retriever.frameAtTime
        retriever.release()
        tempFile.delete()
        bitmap
    } catch (e: IOException) {
        Log.e("MediaItem", "Error extracting video thumbnail", e)
        null
    }
}

@Composable
fun LoadingMessage(msg: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(msg, style = MaterialTheme.typography.bodyLarge)
    }
}
