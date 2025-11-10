package com.fauzangifari.data.repository

import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.data.source.local.room.dao.LetterDao
import com.fauzangifari.data.source.remote.dto.request.SignInRequest
import com.fauzangifari.data.source.remote.dto.request.SignOutRequest
import com.fauzangifari.data.source.remote.retrofit.AuthApiService
import com.fauzangifari.data.utils.AuthTokenProvider
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Auth
import com.fauzangifari.domain.model.Session
import com.fauzangifari.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val authPreferences: AuthPreferences,
    private val letterDao: LetterDao
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Resource<Auth> {
        return try {
            val request = SignInRequest(email, password)
            val response = authApiService.signIn(request)

            val token = response.token
            val userId = response.user.id
            val name = response.user.name
            val userEmail = response.user.email

            authPreferences.saveToken(token)
            authPreferences.saveUserId(userId)
            authPreferences.saveUserName(name)
            authPreferences.saveUserEmail(userEmail)

            AuthTokenProvider.setToken(token)

            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unexpected error")
        }
    }



    override suspend fun signOut(
        success: Boolean
    ): Resource<Boolean> {
        return try {
            val response = authApiService.signOut(SignOutRequest(success = success))
            val isSuccess = response.success ?: false
            val message = response.message

            // Clear preferences dan token
            authPreferences.clear()
            AuthTokenProvider.setToken(null)

            // Clear local cache untuk keamanan data
            letterDao.deleteAllLetters()

            if (isSuccess) {
                Resource.Success(true)
            } else {
                Resource.Error(message ?: "Gagal keluar dari akun")
            }
        } catch (e: Exception) {
            runCatching {
                authPreferences.clear()
                letterDao.deleteAllLetters()
            }
            AuthTokenProvider.setToken(null)
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getSession(): Resource<Session> {
        return try {
            val response = authApiService.getSession()

            if (!response.isSuccessful) {
                val msg = response.errorBody()?.string()
                return Resource.Error("HTTP Error: ${response.code()} - $msg")
            }

            val body = response.body()
            if (body == null) {
                return Resource.Error("Response body is null")
            }

            val sessionDto = body.session
            if (sessionDto == null) {
                return Resource.Error("Session data is missing")
            }

            val session = body.toDomain()
            if (session.token.isBlank()) {
                return Resource.Error("Session token is empty")
            }

            Resource.Success(session)
        } catch (e: IOException) {
            Resource.Error("Network Error: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("HTTP Exception: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unexpected Error: ${e.message}")
        }
    }
}
