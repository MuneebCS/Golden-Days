package com.example.goldendays

import AddEventScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goldendays.screens.EditEventScreen
import com.example.goldendays.screens.EventDetailScreen
import com.example.goldendays.screens.HomeScreen
import com.example.goldendays.screens.MediaDisplayScreen
import com.example.goldendays.screens.MediaPlaybackScreen


@Composable
fun AppNavGraph() {
    // Set up Navigation and Hilt
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
        composable("add_event_screen") {
            AddEventScreen(navController = navController)
        }
        composable("edit_event_screen/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toInt()
            if (eventId != null) {
                EditEventScreen(navController, eventId)
            }
        }
        composable("event_detail_screen/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toInt() ?: 0
            EventDetailScreen(navController, eventId)
        }
        composable("media_display/{mediaId}") { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getString("mediaId")?.toInt() ?: 0
            MediaDisplayScreen(navController, mediaId)
        }
        composable("media_playback/{mediaId}") { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getString("mediaId")?.toInt() ?: 0
            MediaPlaybackScreen(navController, mediaId)
        }


    }
}
