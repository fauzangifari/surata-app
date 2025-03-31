package com.fauzangifari.surata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fauzangifari.surata.ui.screens.detail.DetailScreen
import com.fauzangifari.surata.ui.screens.home.HomeScreen
import com.fauzangifari.surata.ui.screens.notification.NotificationScreen
import com.fauzangifari.surata.ui.screens.profile.ProfileScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Notification.route) { NotificationScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Detail.route) { DetailScreen(navController) }
    }
}