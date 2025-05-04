package com.fauzangifari.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LetterDetailResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("meta")
	val meta: MetaDetail? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<Error?>? = null
) : Parcelable

@Parcelize
data class Result(

	@field:SerializedName("cc")
	val cc: List<String?>? = null,

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("letterDate")
	val letterDate: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("letterContent")
	val letterContent: String? = null,

	@field:SerializedName("reviewer")
	val reviewer: Reviewer? = null,

	@field:SerializedName("isPrinted")
	val isPrinted: Boolean? = null,

	@field:SerializedName("applicant")
	val applicant: Applicant? = null,

	@field:SerializedName("beginDate")
	val beginDate: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("attachment")
	val attachment: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("letterType")
	val letterType: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("letterNumber")
	val letterNumber: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable

@Parcelize
data class ReviewerDetail(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable

@Parcelize
data class ApplicantDetail(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable

@Parcelize
data class MetaDetail(

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("limit")
	val limit: String? = null,

	@field:SerializedName("page")
	val page: String? = null
) : Parcelable
