package com.fauzangifari.surata.data.source.remote.retrofit

import com.fauzangifari.surata.data.source.remote.response.LetterResponse
import retrofit2.http.GET

interface ApiService {
    @GET("letters")
    suspend fun getLetters() : LetterResponse
}