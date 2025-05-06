package com.fauzangifari.surata.ui.screens.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fauzangifari.surata.ui.components.BottomBar
import com.fauzangifari.surata.ui.components.TopBar

@Composable
fun NotificationScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { TopBar(
            navController = navController
        ) },
        bottomBar = { BottomBar(navController) },
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ){

        }
    }
}