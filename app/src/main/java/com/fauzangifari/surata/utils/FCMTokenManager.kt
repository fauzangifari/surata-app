package com.fauzangifari.surata.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object FCMTokenManager {
    private const val TAG = "FCMTokenManager"

    /**
     * Get FCM token asynchronously
     */
    suspend fun getToken(): String? {
        return try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d(TAG, "FCM Token: $token")
            token
        } catch (e: Exception) {
            Log.e(TAG, "Error getting FCM token", e)
            null
        }
    }

    /**
     * Delete FCM token
     */
    suspend fun deleteToken(): Boolean {
        return try {
            FirebaseMessaging.getInstance().deleteToken().await()
            Log.d(TAG, "FCM token deleted")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting FCM token", e)
            false
        }
    }
}

