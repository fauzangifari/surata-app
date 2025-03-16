package com.fauzangifari.surata.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.fauzangifari.surata.ui.screens.home.HomeScreen
import com.fauzangifari.surata.ui.screens.login.LoginScreen
import com.fauzangifari.surata.ui.screens.splash.SplashScreen
import com.fauzangifari.surata.ui.theme.SurataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SurataTheme {
                var showSplash by remember { mutableStateOf(true) }
                val navController = rememberNavController()

                if (showSplash) {
                    SplashScreen { showSplash = false }
                } else {
                    HomeScreen(
                        navController = navController
                    )
//                    LoginScreen()
                }
            }
        }
    }
}