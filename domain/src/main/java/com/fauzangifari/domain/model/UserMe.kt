package com.fauzangifari.domain.model

data class UserMe(
    val id: String,
    val email: String,
    val name: String,
    val image: String?,
    val role: UserRole,
    val studentId: String?,
    val teacherId: String?,
    val student: Student?,
    val teacher: Teacher?,
    val secondaryEmail: String?
)
