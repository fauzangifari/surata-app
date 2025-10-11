package com.fauzangifari.domain.model

data class Session(
    val id: String,
    val expiresAt: String,
    val token: String,
    val ipAddress: String,
    val userAgent: String,
    val userId: String,
    val user: User
)