package com.fauzangifari.surata.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.navigation.Screen

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    var endAnimation by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = startAnimation, label = "Splash Animation")

    val alphaAnim by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "Alpha Animation"
    ) { state -> if (state) 1f else 0f }

    val scaleAnim by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, easing = EaseOutCubic) },
        label = "Scale Animation"
    ) { state -> if (state) 1f else 0.8f }

    val exitAlpha by animateFloatAsState(
        targetValue = if (endAnimation) 0f else alphaAnim,
        animationSpec = tween(durationMillis = 500),
        label = "Exit Alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        endAnimation = true
        delay(500)
        navController.navigate("main_with_bottom_nav") {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.surata_logo),
            contentDescription = "Splash Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(scaleX = scaleAnim, scaleY = scaleAnim)
                .alpha(exitAlpha)
        )
    }
}
