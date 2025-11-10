package com.fauzangifari.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class PresignedResponse(

	@field:SerializedName("result")
	val result: PresignedResult? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("meta")
	val meta: PresignedMeta? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<String?>? = null
)

data class PresignedResult(

	@field:SerializedName("url")
	val url: String? = null
)

data class PresignedMeta(
	val any: List<String>? = null
)
