package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class SessionResponse(
    @SerializedName("session")
    val session: SessionDto? = null,

    @SerializedName("user")
    val user: SessionUserDto? = null
)

data class SessionDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("expiresAt")
    val expiresAt: String? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("ipAddress")
    val ipAddress: String? = null,

    @SerializedName("userAgent")
    val userAgent: String? = null,

    @SerializedName("userId")
    val userId: String? = null
)

data class SessionUserDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("emailVerified")
    val emailVerified: Boolean? = null,

    @SerializedName("secondaryEmail")
    val secondaryEmail: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)