package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.model.Notification
import com.fauzangifari.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<Notification>> {
        return repository.getAllNotifications()
    }
}

