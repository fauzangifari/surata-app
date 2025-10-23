package com.fauzangifari.data.source.remote.retrofit

object AuthTokenProvider {
    @Volatile private var token: String? = null

    fun setToken(newToken: String?) {
        token = newToken
    }

    fun getToken(): String? = token
}