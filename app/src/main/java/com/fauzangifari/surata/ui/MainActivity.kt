package com.fauzangifari.surata.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fauzangifari.surata.ui.navigation.NavigationGraph
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.SurataTheme
import com.fauzangifari.surata.viewmodel.FCMViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val fcmViewModel: FCMViewModel by viewModel()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted")
            // Initialize FCM token
        } else {
            Log.d(TAG, "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        askNotificationPermission()

        handleNotificationIntent()

        setContent {
            SurataTheme {
                NavigationGraph(Screen.Splash.route)
            }
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Notification permission already granted")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

//    private fun initializeFCM() {
//        lifecycleScope.launch {
//            fcmViewModel.initializeFCMToken()
//        }
//    }

    private fun handleNotificationIntent() {
        intent?.extras?.let { extras ->
            val notificationType = extras.getString("notification_type")
            val letterId = extras.getString("letter_id")

            Log.d(TAG, "Notification received - Type: $notificationType, Letter ID: $letterId")

            // TODO: Navigate to appropriate screen based on notification type
            // You can use these values to navigate to specific screens in your app
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}