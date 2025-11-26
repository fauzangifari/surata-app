package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class TeacherResponse(

	@field:SerializedName("result")
	val result: ResultItemTeacher? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<String?>? = null
)

data class ResultItemTeacher(

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("father")
	val father: Father? = null,

	@field:SerializedName("taxNumber")
	val taxNumber: String? = null,

	@field:SerializedName("employmentStatus")
	val employmentStatus: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<Any?>? = null,

	@field:SerializedName("taxName")
	val taxName: String? = null,

	@field:SerializedName("familyCardNumber")
	val familyCardNumber: String? = null,

	@field:SerializedName("religion")
	val religion: String? = null,

	@field:SerializedName("mother")
	val mother: Mother? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("spouseOccupation")
	val spouseOccupation: String? = null,

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("spouseName")
	val spouseName: Any? = null,

	@field:SerializedName("maritalStatus")
	val maritalStatus: String? = null,

	@field:SerializedName("spouseNip")
	val spouseNip: String? = null
)
