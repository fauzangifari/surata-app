package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UsersMeResponse(

	@field:SerializedName("result")
	val result: ResultMe? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<String?>? = null
)

data class ResultMe(

	@field:SerializedName("emailVerified")
	val emailVerified: Boolean? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("secondaryEmail")
	val secondaryEmail: String? = null,

	@field:SerializedName("student")
	val student: ResultItemStudent? = null,

	@field:SerializedName("teacher")
	val teacher: ResultItemTeacher? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)