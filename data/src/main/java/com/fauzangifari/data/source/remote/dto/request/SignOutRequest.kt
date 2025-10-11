package com.fauzangifari.data.source.remote.dto.request

import com.google.gson.annotations.SerializedName

data class SignOutRequest (

    @field:SerializedName("success")
    val success: Boolean

)