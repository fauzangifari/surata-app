package com.fauzangifari.domain.model

data class User(
    val id: String?,
    val email: String?,
    val image: String?,
    val emailVerified: Boolean?,
    val secondaryEmail: String?,
    val name: String?,
)