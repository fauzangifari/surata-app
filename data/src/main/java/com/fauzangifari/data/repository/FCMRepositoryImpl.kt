package com.fauzangifari.data.repository

import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.data.source.remote.dto.request.SaveTokenRequest
import com.fauzangifari.data.source.remote.retrofit.FCMApiService
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.repository.FCMRepository

class FCMRepositoryImpl(
    private val fcmApiService: FCMApiService,
    private val authPreferences: AuthPreferences
) : FCMRepository {

    override suspend fun saveToken(token: String): Resource<String> {
        return try {
            val request = SaveTokenRequest(token = token)
            val response = fcmApiService.saveToken(request)

            if (response.success == true && response.result?.id != null && response.result.token != null) {
                val tokenId = response.result.id
                val tokenFCM = response.result.token
                authPreferences.saveFCMTokenId(tokenId)
                authPreferences.saveFCMToken(tokenFCM)
                Resource.Success(tokenId)
                Resource.Success(tokenFCM)
            } else {
                val errorMessage = response.errors?.firstOrNull() ?: response.message ?: "Failed to save token"
                Resource.Error(errorMessage)
            }
        } catch (e: retrofit2.HttpException) {
            val errorMessage = when (e.code()) {
                404 -> {
                    "HTTP 404 Not Found - Backend endpoint belum diimplementasi. Endpoint yang dibutuhkan: POST /api/notifications/device-token"
                }
                401 -> {
                    "HTTP 401 Unauthorized - Silakan login ulang"
                }
                403 -> {
                    "HTTP 403 Forbidden - Anda tidak memiliki akses"
                }
                500 -> {
                    "HTTP 500 Internal Server Error - Terjadi kesalahan di server"
                }
                else -> {
                    "HTTP ${e.code()} - ${e.message()}"
                }
            }
            Resource.Error(errorMessage)
        } catch (e: java.net.UnknownHostException) {
            val errorMessage = "Tidak dapat terhubung ke server. Periksa koneksi internet Anda."
            Resource.Error(errorMessage)
        } catch (e: java.net.SocketTimeoutException) {
            val errorMessage = "Koneksi timeout. Server tidak merespon."
            Resource.Error(errorMessage)
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "An unexpected error occurred"
            Resource.Error(errorMessage)
        }
    }

    override suspend fun deleteToken(tokenId: String): Resource<Unit> {
        return try {
            fcmApiService.deleteToken(tokenId)
            authPreferences.saveFCMTokenId("")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to delete token")
        }
    }

    companion object {
        private const val TAG = "FCMRepositoryImpl"
    }
}

