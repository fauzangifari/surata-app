package com.fauzangifari.surata.ui

import android.app.Application
import android.util.Log
import com.fauzangifari.data.di.databaseModule
import com.fauzangifari.data.di.networkModule
import com.fauzangifari.data.di.preferencesModule
import com.fauzangifari.data.di.repositoryModule
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.data.utils.AuthTokenProvider
import com.fauzangifari.domain.di.useCaseModule
import com.fauzangifari.domain.usecase.SaveFCMTokenUseCase
import com.fauzangifari.surata.di.viewModelModule
import com.fauzangifari.surata.utils.FCMTokenManager
import com.fauzangifari.surata.utils.NotificationChannelManager
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject

class SurataApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        startKoin {
            androidLogger()
            androidContext(this@SurataApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    preferencesModule,
                    databaseModule
                )
            )
        }

        // Create notification channels
        NotificationChannelManager.createNotificationChannels(this)

        val authPreferences: AuthPreferences by inject(AuthPreferences::class.java)
        val saveFCMTokenUseCase: SaveFCMTokenUseCase by inject(SaveFCMTokenUseCase::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val savedToken = authPreferences.getToken()
            AuthTokenProvider.setToken(savedToken)

            // Get FCM token and save to backend if user is logged in
            if (!savedToken.isNullOrEmpty()) {
                val fcmToken = FCMTokenManager.getToken()
                fcmToken?.let { token ->
                    Log.d(TAG, "FCM Token: $token")
                    // Save token to backend
                    try {
                        val result = saveFCMTokenUseCase(token)
                        Log.d(TAG, "FCM Token save result: $result")
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save FCM token", e)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "SurataApplication"
    }
}
