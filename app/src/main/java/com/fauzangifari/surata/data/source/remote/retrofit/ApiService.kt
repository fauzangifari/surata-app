package com.fauzangifari.surata.data.source.remote.retrofit

import com.fauzangifari.surata.data.source.remote.response.LetterResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("letters")
    suspend fun getLetters() : LetterResponse

    @GET("letters/{letterId}")
    suspend fun getLetterById(
        @Path("letterId") letterId: String
    ) : LetterResponse
}