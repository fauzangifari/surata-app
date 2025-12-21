package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.request.UserRequest
import com.fauzangifari.data.source.remote.dto.response.UsersMeResponse
import com.fauzangifari.data.source.remote.dto.response.UsersResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserApiService {

    @GET("api/users/me")
    suspend fun getUsersMe(): UsersMeResponse

    @GET("api/users")
    suspend fun getAllUsers(): UsersResponse

    @PATCH("api/users/{usersId}")
    suspend fun updateUser(
        @Path("usersId") usersId: String,
        @Body request: UserRequest
    ): UsersMeResponse

}

