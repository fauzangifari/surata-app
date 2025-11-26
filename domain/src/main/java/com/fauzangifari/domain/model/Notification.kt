package com.fauzangifari.domain.model

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val data: String,
    val timestamp: Long,
    val isRead: Boolean = false
)

