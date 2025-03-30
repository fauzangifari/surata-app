package com.fauzangifari.surata.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fauzangifari.surata.ui.navigation.NavigationItem
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem(
            title = "Beranda",
            icon = R.drawable.ic_outline_home_24,
            screen = Screen.Home
        ),
        NavigationItem(
            title = "Notifikasi",
            icon = R.drawable.ic_outline_notifications_24,
            screen = Screen.Notification
        ),
        NavigationItem(
            title = "Profil",
            icon = R.drawable.ic_outline_person_24,
            screen = Screen.Profile
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = White,
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route


            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (isSelected) Blue800 else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontFamily = PlusJakartaSans,
                        color = if (isSelected) Blue800 else Color.Gray
                    )
                }
            )
        }
    }
}
