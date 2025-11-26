package com.fauzangifari.domain.model

data class Student(
    val id: String? = null,
    val userId: String? = null,
    val nik: String? = null,
    val nisn: String? = null,
    val nipd: String? = null,
    val name: String? = null,
    val birthPlace: String? = null,
    val birthDate: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    val address: Address? = null,
)