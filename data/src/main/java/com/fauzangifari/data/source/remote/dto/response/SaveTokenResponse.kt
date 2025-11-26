package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class SaveTokenResponse(

	@field:SerializedName("result")
	val result: ResultSaveToken? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<String?>? = null
)

data class ResultSaveToken(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("lastSeen")
	val lastSeen: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("deviceInfo")
	val deviceInfo: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
