package com.fauzangifari.domain.repository

import com.fauzangifari.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<Notification>>
    suspend fun getNotificationById(id: String): Notification?
    suspend fun insertNotification(notification: Notification)
    suspend fun markAsRead(id: String)
    suspend fun markAllAsRead()
    suspend fun deleteNotification(id: String)
    suspend fun deleteAllNotifications()
    fun getUnreadCount(): Flow<Int>
}

