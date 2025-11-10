package com.fauzangifari.domain.model

data class Letter(
    val id: String,
    val applicantName: String,
    val applicantEmail: String,
    val subject: String,
    val endDate: String,
    val letterContent: String,
    val isPrinted: Boolean,
    val beginDate: String,
    val createdAt: String,
    val attachment: String,
    val letterType: String,
    val status: String,
    val letterNumber: String,
    val cc: List<String> = emptyList(),
    val reason: String? = null
)