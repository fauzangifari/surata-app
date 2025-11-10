package com.fauzangifari.data.source.remote.dto.request

import com.google.gson.annotations.SerializedName

data class PresignedRequest(

	@field:SerializedName("fileName")
	val fileName: String? = null,

	@field:SerializedName("fileSize")
	val fileSize: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("fileType")
	val fileType: String? = null
)
