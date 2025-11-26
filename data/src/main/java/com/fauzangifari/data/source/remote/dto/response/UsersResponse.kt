package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UsersResponse(

    @field:SerializedName("result")
    val result: List<ResultUsers?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("meta")
    val meta: Meta? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("errors")
    val errors: List<String?>? = null
)

data class ResultUsers(

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
