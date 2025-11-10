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

data class Address(
    val country: String? = null,
    val rt: String? = null,
    val province: String? = null,
    val rw: String? = null,
    val city: String? = null,
    val street: String? = null,
    val district: String? = null,
    val postalCode: String? = null,
    val location: Location? = null,
    val subDistrict: String? = null
)

data class Location(
    val coordinate: List<Double?>? = null,
    val type: String? = null
)