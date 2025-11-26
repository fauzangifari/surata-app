package com.fauzangifari.domain.model

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