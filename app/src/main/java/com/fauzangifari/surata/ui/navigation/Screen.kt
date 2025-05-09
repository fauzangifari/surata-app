package com.fauzangifari.surata.ui.navigation

sealed class Screen(val route: String){
    object Splash: Screen("splash")
    object Login: Screen("login")
    object Home: Screen("home")
    object Notification: Screen("notification")
    object Profile: Screen("profile")
    object Detail : Screen("detail/{letterId}") {
        fun passId(letterId: String): String = "detail/$letterId"
    }
    object Setting: Screen("setting")
}