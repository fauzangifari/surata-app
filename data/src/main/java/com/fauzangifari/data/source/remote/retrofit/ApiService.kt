package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.response.LetterDetailResponse
import com.fauzangifari.data.source.remote.response.LetterResponse
import com.fauzangifari.data.source.remote.response.StudentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("v1/letters")
    suspend fun getLetters() : LetterResponse

    @GET("v1/letters/{letterId}")
    suspend fun getLetterById(
        @Path("letterId") letterId: String
    ) : LetterDetailResponse

    @GET("v1/students")
    suspend fun getStudents() : StudentResponse

}