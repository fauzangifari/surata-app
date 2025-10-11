package com.fauzangifari.domain.model

data class Auth(
    val token: String,
    val user: User
)