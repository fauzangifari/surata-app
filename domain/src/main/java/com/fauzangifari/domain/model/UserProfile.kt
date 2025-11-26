package com.fauzangifari.domain.model

data class UserProfile(
    val name: String = "",
    val schoolEmail: String = "",
    val personalEmail: String = "",
    val placeOfBirth: String = "",
    val dateOfBirth: String = "",
    val phone: String = "",
    val idNumber: String = "",
    val photoUrl: String? = null
)