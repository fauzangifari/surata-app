package com.fauzangifari.domain.model

data class ReqLetter(
    val subject: String?,
    val letterType: String,
    val beginDate: String?,
    val endDate: String?,
    val attachment: String?,
    val reason: String?,
    val cc: List<String>?,
    val isPrinted: Boolean
)