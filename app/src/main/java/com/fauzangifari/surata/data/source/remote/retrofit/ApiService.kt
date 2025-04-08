package com.fauzangifari.surata.data.source.remote.retrofit

import com.fauzangifari.surata.data.source.remote.response.LetterDetailResponse
import com.fauzangifari.surata.data.source.remote.response.LetterResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v1/letters")
    suspend fun getLetters() : LetterResponse

    @GET("v1/letters/{letterId}")
    suspend fun getLetterById(
        @Path("letterId") letterId: String
    ) : LetterDetailResponse
}