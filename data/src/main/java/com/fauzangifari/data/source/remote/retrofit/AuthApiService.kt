package com.fauzangifari.data.source.remote.retrofit

import com.fauzangifari.data.source.remote.dto.request.SignInRequest
import com.fauzangifari.data.source.remote.dto.request.SignOutRequest
import com.fauzangifari.data.source.remote.dto.response.SessionResponse
import com.fauzangifari.data.source.remote.dto.response.SignInResponse
import com.fauzangifari.data.source.remote.dto.response.SignOutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/sign-in/email")
    suspend fun signIn(
        @Body request: SignInRequest
    ): SignInResponse

    @POST("api/auth/sign-out")
    suspend fun signOut(
        @Body request: SignOutRequest
    ): SignOutResponse

    @GET("api/auth/get-session")
    suspend fun getSession(): Response<SessionResponse>

}
