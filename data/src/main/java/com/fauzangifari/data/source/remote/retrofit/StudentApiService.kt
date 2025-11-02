package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.response.StudentResponse
import retrofit2.http.GET

interface StudentApiService {

    @GET("/api/students")
    suspend fun getAllStudent(): StudentResponse

}