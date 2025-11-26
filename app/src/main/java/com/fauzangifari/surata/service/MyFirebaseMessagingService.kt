package com.fauzangifari.surata.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Notification
import com.fauzangifari.domain.usecase.SaveFCMTokenUseCase
import com.fauzangifari.domain.usecase.SaveNotificationUseCase
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.MainActivity
import com.fauzangifari.surata.utils.NotificationChannelManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.json.JSONObject
import java.util.UUID
import androidx.core.content.edit

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val saveFCMTokenUseCase: SaveFCMTokenUseCase by inject()
    private val saveNotificationUseCase: SaveNotificationUseCase by inject()
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token received: $token")

        saveTokenLocally(token)

        serviceScope.launch {
            try {
                val result = saveFCMTokenUseCase(token)
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "✅ Token successfully saved to backend with ID: ${result.data}")
                    }
                    is Resource.Error -> {
                        val errorMessage = result.message ?: "Unknown error"
                        when {
                            errorMessage.contains("404") -> {
                                Log.w(TAG, "⚠️ Backend endpoint not implemented yet (404)")
                                Log.w(TAG, "   Expected endpoint: POST /api/notifications/device-token")
                                Log.w(TAG, "   Token saved locally. Notifications will still work via Firebase.")
                            }
                            errorMessage.contains("401") || errorMessage.contains("403") -> {
                                Log.w(TAG, "⚠️ Authentication failed. User might need to login again.")
                            }
                            errorMessage.contains("Unable to resolve host") ||
                            errorMessage.contains("Failed to connect") -> {
                                Log.w(TAG, "⚠️ Backend server unreachable. Token saved locally.")
                            }
                            else -> {
                                Log.e(TAG, "❌ Failed to save token to backend: $errorMessage")
                            }
                        }
                    }
                    else -> {
                        Log.w(TAG, "⚠️ Unexpected result when saving token")
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "⚠️ Exception saving token to backend: ${e.message}")
                Log.w(TAG, "   Token saved locally. App will continue to work normally.")
            }
        }
    }

    private fun saveTokenLocally(token: String) {
        try {
            getSharedPreferences("fcm_prefs", MODE_PRIVATE)
                .edit {
                    putString("fcm_token", token)
                        .putLong("fcm_token_timestamp", System.currentTimeMillis())
                }
            Log.d(TAG, "Token saved to local storage as backup")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save token locally", e)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            val title = it.title ?: "Surata"
            val body = it.body ?: ""

            val dataJson = try {
                JSONObject(message.data as Map<*, *>).toString()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to convert payload to JSON", e)
                message.data.toString()
            }

            saveNotificationToDatabase(
                title = title,
                message = body,
                data = dataJson
            )

            sendNotification(
                title = title,
                body = body,
                data = message.data
            )
        }
    }

    private fun saveNotificationToDatabase(
        title: String,
        message: String,
        data: String
    ) {
        serviceScope.launch {
            try {
                val notification = Notification(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    message = message,
                    data = data,
                    timestamp = System.currentTimeMillis(),
                    isRead = false
                )

                saveNotificationUseCase(notification)
                Log.d(TAG, "Notification saved to database: $title")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save notification to database", e)
            }
        }
    }

    private fun sendNotification(title: String, body: String, data: Map<String, String>) {
        val notificationType = data["notification_type"]

        val channelId = NotificationChannelManager.getChannelIdForType(notificationType)

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationPriority = when (data["priority"]) {
            "high" -> NotificationCompat.PRIORITY_HIGH
            "low" -> NotificationCompat.PRIORITY_LOW
            else -> NotificationCompat.PRIORITY_DEFAULT
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(notificationPriority)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.getNotificationChannel(channelId) == null) {
            NotificationChannelManager.createNotificationChannels(this)
        }

        val notificationId = System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, notificationBuilder.build())

        Log.d(TAG, "Notification sent: $title")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    companion object {
        private const val TAG = "FCMService"
    }
}
