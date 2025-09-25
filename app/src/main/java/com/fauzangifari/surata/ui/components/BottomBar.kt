package com.fauzangifari.surata.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.navigation.NavigationItem
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem("Beranda", R.drawable.ic_outline_home_24, Screen.Home),
        NavigationItem("Notifikasi", R.drawable.ic_outline_notifications_24, Screen.Notification),
        NavigationItem("Profil", R.drawable.ic_outline_person_24, Screen.Profile),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = modifier,
        containerColor = White,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { item ->
                val isSelected = currentRoute == item.screen.route

                val animatedPadding by animateDpAsState(
                    targetValue = if (isSelected) 16.dp else 12.dp,
                    animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                    label = "padding"
                )

                val animatedBgColor by animateColorAsState(
                    targetValue = if (isSelected) Blue800.copy(alpha = 0.15f) else Color.Transparent,
                    animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                    label = "bgColor"
                )

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(animatedBgColor)
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                            }
                        }
                        .padding(horizontal = animatedPadding, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = if (isSelected) Blue800 else Color.Black,
                        )
                        AnimatedContent(
                            targetState = isSelected,
                            transitionSpec = {
                                (fadeIn(
                                    animationSpec = tween(400, easing = LinearOutSlowInEasing)
                                ) + slideInHorizontally(
                                    initialOffsetX = { it / 2 },
                                    animationSpec = tween(400, easing = LinearOutSlowInEasing)
                                )).togetherWith(
                                    fadeOut(
                                        animationSpec = tween(300, easing = FastOutLinearInEasing)
                                    ) + slideOutHorizontally(
                                        targetOffsetX = { -it / 2 },
                                        animationSpec = tween(300, easing = FastOutLinearInEasing)
                                    )
                                )
                            },
                            label = "textTransition"
                        ) { visible ->
                            if (visible) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Medium,
                                    color = Blue800
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

