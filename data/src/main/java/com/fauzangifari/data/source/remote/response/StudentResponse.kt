package com.fauzangifari.data.source.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class StudentResponse(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("meta")
	val meta: MetaStudent? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<Error?>? = null
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class ResultItemStudent(

	@field:SerializedName("siblings")
	val siblings: String? = null,

	@field:SerializedName("distance")
	val distance: Long? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("father")
	val father: Father? = null,

	@field:SerializedName("kpsPkh")
	val kpsPkh: Boolean? = null,

	@field:SerializedName("bloodType")
	val bloodType: String? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<String?>? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("mother")
	val mother: Mother? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("kip")
	val kip: Boolean? = null,

	@field:SerializedName("pip")
	val pip: Boolean? = null,

	@field:SerializedName("nis")
	val nis: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("guardian")
	val guardian: Guardian? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("travelTime")
	val travelTime: Int? = null,

	@field:SerializedName("living")
	val living: String? = null,

	@field:SerializedName("headCircumference")
	val headCircumference: Int? = null,

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("nisn")
	val nisn: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("familyCardNumber")
	val familyCardNumber: String? = null,

	@field:SerializedName("transportation")
	val transportation: String? = null,

	@field:SerializedName("religion")
	val religion: String? = null,

	@field:SerializedName("birthCertificateNumber")
	val birthCertificateNumber: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("landline")
	val landline: String? = null
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
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
	val location: List<String?>? = null,

	@field:SerializedName("subDistrict")
	val subDistrict: String? = null
) : Parcelable

@Parcelize
data class MetaStudent(

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("limit")
	val limit: String? = null,

	@field:SerializedName("page")
	val page: String? = null
) : Parcelable
