package com.fauzangifari.data.utils

import android.util.Log
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import com.fauzangifari.domain.BuildConfig

class AuthInterceptor(
    private val authPreferences: AuthPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = AuthTokenProvider.getToken() ?: runBlocking {
            authPreferences.getToken()
        }

        val newRequest = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                header("Authorization", "Bearer $token")
                header("Accept", "application/json")
                if (BuildConfig.DEBUG) {
                    Log.d("AuthInterceptor", "Authorization header added")
                }
            }
        }.build()

        return chain.proceed(newRequest)
    }
}
