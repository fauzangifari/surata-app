package com.fauzangifari.domain.model

data class Presigned(
    val success: Boolean? = null,
    val url: String? = null,
    val message: String? = null,
    val errors: List<String?>? = null
)