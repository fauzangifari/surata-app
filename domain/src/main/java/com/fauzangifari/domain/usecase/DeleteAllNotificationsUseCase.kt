package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.repository.NotificationRepository

class DeleteAllNotificationsUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke() {
        notificationRepository.deleteAllNotifications()
    }
}

