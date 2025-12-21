package com.fauzangifari.data.source.remote.dto.request

import com.google.gson.annotations.SerializedName

data class UserRequest(

	@field:SerializedName("emailVerified")
	val emailVerified: Boolean? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("secondaryEmail")
	val secondaryEmail: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null
)
