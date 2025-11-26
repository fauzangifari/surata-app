package com.fauzangifari.data.mapper

import com.fauzangifari.data.source.local.room.entity.NotificationEntity
import com.fauzangifari.domain.model.Notification

fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = this.id,
        title = this.title,
        message = this.message,
        data = this.data,
        timestamp = this.timestamp,
        isRead = this.isRead
    )
}

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        title = this.title,
        message = this.message,
        data = this.data,
        timestamp = this.timestamp,
        isRead = this.isRead
    )
}

