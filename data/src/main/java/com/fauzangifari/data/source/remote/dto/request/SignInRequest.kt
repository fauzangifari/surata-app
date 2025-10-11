package com.fauzangifari.data.source.remote.dto.request

import com.google.gson.annotations.SerializedName

data class SignInRequest(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("callbackUrl")
    val callBackURL: String? = null,

    @field:SerializedName("rememberMe")
    val rememberMe: String? = null
)