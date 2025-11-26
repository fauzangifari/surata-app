package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.request.SaveTokenRequest
import com.fauzangifari.data.source.remote.dto.response.SaveTokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface FCMApiService {
    @POST("api/notifications/device-token/{tokenId}")
    suspend fun saveToken(
        @Body request: SaveTokenRequest
    ): SaveTokenResponse

    @DELETE("api/notifications/device-token/{tokenId}")
    suspend fun deleteToken(
        @Path("tokenId") tokenId: String
    )
}