package com.fauzangifari.domain.model

data class SignInParam(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)