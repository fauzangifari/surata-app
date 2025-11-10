package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.request.LetterRequest
import com.fauzangifari.data.source.remote.dto.request.PresignedRequest
import com.fauzangifari.data.source.remote.dto.response.LetterDetailResponse
import com.fauzangifari.data.source.remote.dto.response.LetterResponse
import com.fauzangifari.data.source.remote.dto.response.PresignedResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LetterApiService {

    @GET("/api/letters")
    suspend fun getLetters() : LetterResponse

    @GET("api/users/{userId}/letters")
    suspend fun getLettersByUserId(
        @Path("userId") userId: String
    ) : LetterResponse

    @GET("/api/letters/{letterId}")
    suspend fun getLetterById(
        @Path("letterId") letterId: String
    ) : LetterDetailResponse

    @POST("/api/letters")
    suspend fun postLetter(
        @Body request: LetterRequest
    ) : LetterDetailResponse

    @POST("/api/presigned")
    suspend fun postPresignedUrl(
        @Body request: PresignedRequest
    ) : PresignedResponse
}