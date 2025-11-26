package com.fauzangifari.domain.model

data class Teacher(
    val id: String? = null,
    val userId: String? = null,
    val nik: String? = null,
    val nip: String? = null,
    val birthPlace: String? = null,
    val birthDate: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val address: Address? = null,
    val religion: String? = null,
    val nationality: String? = null,
    val employmentStatus: String? = null,
    val maritalStatus: String? = null,
    val taxNumber: String? = null,
    val taxName: String? = null,
    val familyCardNumber: String? = null,
    val spouseName: String? = null,
    val spouseNip: String? = null,
    val spouseOccupation: String? = null
)
