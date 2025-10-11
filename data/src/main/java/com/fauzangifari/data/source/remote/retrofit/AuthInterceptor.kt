package com.fauzangifari.data.source.remote.retrofit

import android.util.Log
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import com.fauzangifari.domain.BuildConfig

class AuthInterceptor(
    private val authPreferences: AuthPreferences
) : Interceptor {

    @Volatile
    private var cachedToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = cachedToken ?: runBlocking {
            authPreferences.token.firstOrNull()?.also {
                cachedToken = it
            }
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
