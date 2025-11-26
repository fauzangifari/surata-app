package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.model.Notification
import com.fauzangifari.domain.repository.NotificationRepository

class SaveNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: Notification) {
        repository.insertNotification(notification)
    }
}

