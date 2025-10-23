package com.fauzangifari.data.source.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LetterRequest(

	@field:SerializedName("cc")
	val cc: List<String> = emptyList(),

	@field:SerializedName("beginDate")
	val beginDate: String? = null,

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("attachment")
	val attachment: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("isPrinted")
	val isPrinted: Boolean? = null,

	@field:SerializedName("letterType")
	val letterType: String? = null
)
