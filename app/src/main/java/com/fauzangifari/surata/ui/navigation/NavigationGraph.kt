package com.fauzangifari.surata.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.fauzangifari.surata.ui.components.BottomBar
import com.fauzangifari.surata.ui.components.TopBar
import com.fauzangifari.surata.ui.screens.detail.DetailScreen
import com.fauzangifari.surata.ui.screens.home.HomeScreen
import com.fauzangifari.surata.ui.screens.home.HomeViewModel
import com.fauzangifari.surata.ui.screens.login.LoginScreen
import com.fauzangifari.surata.ui.screens.notification.NotificationScreen
import com.fauzangifari.surata.ui.screens.profile.ProfileScreen
import com.fauzangifari.surata.ui.screens.settings.SettingScreen
import com.fauzangifari.surata.ui.screens.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen()
        }

        composable("main_with_bottom_nav") {
            BottomBarLayout(navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("letterId") { type = NavType.StringType }),
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing)) +
                        slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing)) +
                        slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        )
            }
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString("letterId") ?: return@composable
            DetailScreen(navController = navController, letterId = letterId)
        }

        composable(
            route = Screen.Setting.route,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing)) +
                        slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing)) +
                        slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        )
            }
        ) {
            SettingScreen(
                navController = navController
            )
        }
    }
}


@Composable
fun BottomBarLayout(rootNavController: NavHostController) {
    val navController = rememberNavController()
    val bottomBarScreens = listOf(Screen.Home, Screen.Notification, Screen.Profile)

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            TopBar(
                navController = rootNavController
            )
        },
        bottomBar = {
            if (currentRoute in bottomBarScreens.map {
                it.route
            }) {
                BottomBar(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val viewModel: HomeViewModel = koinViewModel()
                HomeScreen(navController = rootNavController, viewModel = viewModel)
            }
            composable(Screen.Notification.route) {
                NotificationScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}