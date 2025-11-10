package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.response.StudentResponse
import retrofit2.http.GET
import retrofit2.http.PATCH

interface StudentApiService {

    @GET("/api/students")
    suspend fun getAllStudent(): StudentResponse

}