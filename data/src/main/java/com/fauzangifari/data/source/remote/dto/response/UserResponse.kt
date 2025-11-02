package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("image")
    val image: String?,

    @field:SerializedName("emailVerified")
    val emailVerified: Boolean,

    @field:SerializedName("secondaryEmail")
    val secondaryEmail: String?,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)