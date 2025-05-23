package com.fauzangifari.surata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.Grey700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    onSettingClick: () -> Unit = { navController.navigate(Screen.Setting.route) },
    onNotificationClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.surata_logo_2),
                contentDescription = "Logo Surata",
                modifier = Modifier.size(80.dp)
            )
        },
        actions = {
            IconButton(onClick = { onNotificationClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_notifications_24),
                    contentDescription = "Notifikasi",
                    tint = Grey700
                )
            }
            IconButton(onClick = { onSettingClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_settings_24),
                    contentDescription = "Pengaturan",
                    tint = Grey700
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}