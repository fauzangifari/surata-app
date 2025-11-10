package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class StudentResponse(

	@field:SerializedName("result")
	val result: List<ResultItemStudent?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<Any?>? = null
)

data class Mother(

	@field:SerializedName("income")
	val income: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("education")
	val education: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<String?>? = null
)

data class Location(

	@field:SerializedName("coordinates")
	val coordinates: List<Double?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Father(

	@field:SerializedName("income")
	val income: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("education")
	val education: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<String?>? = null
)

data class ResultItemStudent(

	@field:SerializedName("birthOrder")
	val birthOrder: Int? = null,

	@field:SerializedName("siblings")
	val siblings: String? = null,

	@field:SerializedName("distance")
	val distance: Long? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("father")
	val father: Father? = null,

	@field:SerializedName("transportationMode")
	val transportationMode: String? = null,

	@field:SerializedName("bloodType")
	val bloodType: Any? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<String?>? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("mother")
	val mother: Mother? = null,

	@field:SerializedName("residenceType")
	val residenceType: String? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("nipd")
	val nipd: String? = null,

	@field:SerializedName("guardian")
	val guardian: Guardian? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("travelTime")
	val travelTime: Int? = null,

	@field:SerializedName("headCircumference")
	val headCircumference: Int? = null,

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("nisn")
	val nisn: String? = null,

	@field:SerializedName("numberOfSiblings")
	val numberOfSiblings: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("skhun")
	val skhun: String? = null,

	@field:SerializedName("familyCardNumber")
	val familyCardNumber: String? = null,

	@field:SerializedName("religion")
	val religion: String? = null,

	@field:SerializedName("birthCertificateNumber")
	val birthCertificateNumber: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("landline")
	val landline: String? = null,

	@field:SerializedName("hobby")
	val hobby: String? = null
)

data class Address(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("rt")
	val rt: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("rw")
	val rw: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("postalCode")
	val postalCode: String? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("subDistrict")
	val subDistrict: String? = null
)

data class Guardian(

	@field:SerializedName("income")
	val income: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("education")
	val education: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<String?>? = null
)

data class Meta(
	val any: Any? = null
)
