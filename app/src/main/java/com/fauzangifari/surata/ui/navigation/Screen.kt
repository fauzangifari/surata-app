package com.fauzangifari.surata.ui.navigation

sealed class Screen(val route: String){
    object Home: Screen("home")
    object Notification: Screen("notification")
    object Profile: Screen("profile")
    object Detail: Screen("detail")
}