package com.fauzangifari.surata.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.BottomBar
import com.fauzangifari.surata.ui.components.TopBar
import com.fauzangifari.surata.ui.theme.Blue800

@Composable
fun ProfileScreen(navController: NavHostController, modifier: Modifier = Modifier) {
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