package com.fauzangifari.surata.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fauzangifari.surata.ui.components.BottomBar
import com.fauzangifari.surata.ui.components.TopBar
import com.fauzangifari.surata.ui.screens.about.AboutScreen
import com.fauzangifari.surata.ui.screens.detail.DetailScreen
import com.fauzangifari.surata.ui.screens.faq.FAQScreen
import com.fauzangifari.surata.ui.screens.faq.FAQViewModel
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

        composable(
            route = Screen.Splash.route,
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            SplashScreen(navController)
        }

        composable(
            route = Screen.Login.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main_with_bottom_nav") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = "main_with_bottom_nav",
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            }
        ) {
            BottomBarLayout(navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("letterId") { type = NavType.StringType }),
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut(animationSpec = tween(400))
            }
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString("letterId") ?: return@composable
            DetailScreen(navController = navController, letterId = letterId)
        }

        composable(
            route = Screen.Setting.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            SettingScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.FAQ.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            val viewModel: FAQViewModel = koinViewModel()
            FAQScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.About.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            AboutScreen(
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
                navController = rootNavController,
            )
        },
        bottomBar = {
            if (currentRoute in bottomBarScreens.map {
                it.route
            }) {
                BottomBar(
                    navController = navController,
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(
                route = Screen.Home.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.Notification.route -> slideIntoContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        ) + fadeIn(animationSpec = tween(400))
                        Screen.Profile.route -> slideIntoContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        ) + fadeIn(animationSpec = tween(400))
                        else -> fadeIn(animationSpec = tween(400))
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.Notification.route -> slideOutOfContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeOut(animationSpec = tween(400))
                        Screen.Profile.route -> slideOutOfContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeOut(animationSpec = tween(400))
                        else -> fadeOut(animationSpec = tween(300))
                    }
                }
            ) {
                val viewModel: HomeViewModel = koinViewModel()
                HomeScreen(navController = rootNavController, viewModel = viewModel)
            }

            composable(
                route = Screen.Notification.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.Home.route -> slideIntoContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeIn(animationSpec = tween(400))
                        Screen.Profile.route -> slideIntoContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        ) + fadeIn(animationSpec = tween(400))
                        else -> fadeIn(animationSpec = tween(400))
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.Home.route -> slideOutOfContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        ) + fadeOut(animationSpec = tween(400))
                        Screen.Profile.route -> slideOutOfContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeOut(animationSpec = tween(400))
                        else -> fadeOut(animationSpec = tween(300))
                    }
                }
            ) {
                NotificationScreen()
            }

            composable(
                route = Screen.Profile.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.Home.route -> slideIntoContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeIn(animationSpec = tween(400))
                        Screen.Notification.route -> slideIntoContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        ) + fadeIn(animationSpec = tween(400))
                        else -> fadeIn(animationSpec = tween(400))
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.Home.route -> slideOutOfContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        ) + fadeOut(animationSpec = tween(400))
                        Screen.Notification.route -> slideOutOfContainer(
                            animationSpec = tween(400),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        ) + fadeOut(animationSpec = tween(400))
                        else -> fadeOut(animationSpec = tween(300))
                    }
                }
            ) {
                ProfileScreen()
            }
        }
    }
}