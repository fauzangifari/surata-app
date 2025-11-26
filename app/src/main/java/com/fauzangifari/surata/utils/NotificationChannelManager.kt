package com.fauzangifari.surata.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationChannelManager {

    const val CHANNEL_ID = "surata_notification_channel"

    fun createNotificationChannels(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Notifikasi Surata",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Semua notifikasi dari aplikasi Surata"
            enableLights(true)
            enableVibration(true)
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun getChannelIdForType(notificationType: String?): String {
        return CHANNEL_ID
    }

    @Suppress("unused")
    fun deleteAllChannels(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(CHANNEL_ID)
    }
}

