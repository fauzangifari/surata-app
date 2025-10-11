package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.response.LetterDetailResponse
import com.fauzangifari.data.source.remote.dto.response.LetterResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LetterApiService {

    @GET("v1/letters")
    suspend fun getLetters() : LetterResponse

    @GET("v1/letters/{letterId}")
    suspend fun getLetterById(
        @Path("letterId") letterId: String
    ) : LetterDetailResponse

}