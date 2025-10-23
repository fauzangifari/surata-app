package com.fauzangifari.surata.ui.screens.splash

import android.util.Log
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
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.navigation.Screen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import com.fauzangifari.domain.common.Resource

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = koinViewModel()
) {
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

    val sessionState by viewModel.sessionState.collectAsState()

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        viewModel.checkSession()
    }

    LaunchedEffect(sessionState) {
        when (sessionState) {
            is Resource.Success -> {
                delay(1000)
                endAnimation = true
                delay(500)
                navController.navigate("main_with_bottom_nav") {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }

            is Resource.Error -> {
                delay(1000)
                endAnimation = true
                delay(500)
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }

            else -> Unit
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

