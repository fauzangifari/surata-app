package com.fauzangifari.surata.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fauzangifari.surata.ui.navigation.NavigationGraph
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.SurataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SurataTheme {
                NavigationGraph(Screen.Splash.route)
            }
        }
    }
}