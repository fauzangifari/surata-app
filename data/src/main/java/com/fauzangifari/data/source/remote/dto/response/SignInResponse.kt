package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @field:SerializedName("redirect")
    val redirect: Boolean,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("url")
    val url: String?,

    @field:SerializedName("user")
    val user: UserResponse
)

