package com.fauzangifari.data.repository

import android.util.Log
import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.data.source.remote.dto.request.SignInRequest
import com.fauzangifari.data.source.remote.retrofit.AuthApiService
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Auth
import com.fauzangifari.domain.model.Session
import com.fauzangifari.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val authPreferences: AuthPreferences
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Resource<Auth> {
        return try {
            return try {
                val request = SignInRequest(email, password)
                val response = authApiService.signIn(request)
                authPreferences.saveToken(response.token)
                Log.i("AuthRepositoryImpl", "Token saved: ${response.token}")
                Log.i("AuthRepositoryImpl", "Response: $response")
                Resource.Success(response.toDomain())
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun signOut(): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getSession(): Resource<Session> {
        return try {
            val response = authApiService.getSession()

            Log.d("AuthRepositoryImpl", "Raw Response: $response")
            Log.d("AuthRepositoryImpl", "Response Body: ${response.body()}")

            if (!response.isSuccessful) {
                val msg = response.errorBody()?.string()
                Log.e("AuthRepositoryImpl", "HTTP Error: ${response.code()} - $msg")
                return Resource.Error("HTTP Error: ${response.code()} - $msg")
            }

            val body = response.body()
            if (body == null) {
                Log.e("AuthRepositoryImpl", "Body is null")
                return Resource.Error("Response body is null")
            }

            val sessionDto = body.session
            if (sessionDto == null) {
                Log.e("AuthRepositoryImpl", "Session data is null: $body")
                return Resource.Error("Session data is missing")
            }

            val session = body.toDomain()
            if (session.token.isBlank()) {
                Log.e("AuthRepositoryImpl", "Session token is blank: $body")
                return Resource.Error("Session token is empty")
            }

            Log.d("AuthRepositoryImpl", "Session Response: $body")
            Resource.Success(session)
        } catch (e: IOException) {
            Log.e("AuthRepositoryImpl", "Network Error: ${e.message}", e)
            Resource.Error("Network Error: ${e.message}")
        } catch (e: HttpException) {
            Log.e("AuthRepositoryImpl", "HTTP Exception: ${e.message}", e)
            Resource.Error("HTTP Exception: ${e.message}")
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Unexpected Error: ${e.message}", e)
            Resource.Error("Unexpected Error: ${e.message}")
        }
    }
}