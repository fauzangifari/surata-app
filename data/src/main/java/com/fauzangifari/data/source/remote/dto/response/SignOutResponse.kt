package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class SignOutResponse(
    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
