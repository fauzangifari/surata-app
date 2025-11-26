package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.response.UsersMeResponse
import com.fauzangifari.data.source.remote.dto.response.UsersResponse
import retrofit2.http.GET

interface UserApiService {

    @GET("api/users/me")
    suspend fun getUsersMe(): UsersMeResponse

    @GET("api/users")
    suspend fun getAllUsers(): UsersResponse

}

