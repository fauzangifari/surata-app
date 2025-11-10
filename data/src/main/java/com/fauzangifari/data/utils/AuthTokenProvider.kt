package com.fauzangifari.data.utils

object AuthTokenProvider {
    @Volatile private var token: String? = null

    fun setToken(newToken: String?) {
        token = newToken
    }

    fun getToken(): String? = token
}