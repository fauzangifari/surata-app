package com.fauzangifari.data.source.remote.dto.request

import com.google.gson.annotations.SerializedName

data class SaveTokenRequest(
    @field:SerializedName("token")
    val token: String? = null,
)