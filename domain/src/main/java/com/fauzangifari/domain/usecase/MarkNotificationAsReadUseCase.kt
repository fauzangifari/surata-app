package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.repository.NotificationRepository

class MarkNotificationAsReadUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: String) {
        repository.markAsRead(id)
    }
}

