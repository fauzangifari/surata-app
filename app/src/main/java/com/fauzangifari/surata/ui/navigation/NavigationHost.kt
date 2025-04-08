package com.fauzangifari.surata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fauzangifari.surata.ui.screens.detail.DetailScreen
import com.fauzangifari.surata.ui.screens.home.HomeScreen
import com.fauzangifari.surata.ui.screens.home.HomeViewModel
import com.fauzangifari.surata.ui.screens.notification.NotificationScreen
import com.fauzangifari.surata.ui.screens.profile.ProfileScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel  = hiltViewModel()
            HomeScreen(navController, viewModel)
        }
        composable(Screen.Notification.route) { NotificationScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("letterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString("letterId") ?: return@composable
            DetailScreen(navController = navController, letterId = letterId)
        }
    }
}