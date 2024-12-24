package com.example.goldendays.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.goldendays.AppModule.MediaViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPlaybackScreen(navController: NavController, mediaId: Int) {
    val mediaViewModel: MediaViewModel = hiltViewModel()
    val media = mediaViewModel.media.collectAsState().value
    val isLoading = mediaViewModel.isLoading.collectAsState().value
    val error = mediaViewModel.error.collectAsState().value
    val context = LocalContext.current

    val videoUri = remember(media?.uri) {
        media?.uri?.let { saveByteArrayAsTempFile(context, it).toUri() } ?: Uri.EMPTY
    }

    if (media == null || media.mediaId != mediaId) {
        mediaViewModel.loadMedia(mediaId)
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
                    mediaViewModel.deleteMedia(media!!)
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Media")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when {
                isLoading -> Text("Loading media...", modifier = Modifier.align(Alignment.Center))
                error != null -> Text("Error loading media: $error", modifier = Modifier.align(Alignment.Center))
                media != null && media.mediaId == mediaId && media.type == "video" -> VideoPlayer(uri = videoUri)
            }
        }
    }
}

private fun saveByteArrayAsTempFile(context: Context, byteArray: ByteArray): File {
    val tempFile = File(context.cacheDir, "temp_video.mp4")
    tempFile.deleteOnExit()
    FileOutputStream(tempFile).use { it.write(byteArray) }
    return tempFile
}

@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = { ctx -> PlayerView(ctx).apply { player = exoPlayer } },
        modifier = Modifier.fillMaxSize()
    )
}
