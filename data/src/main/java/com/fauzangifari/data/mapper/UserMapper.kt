package com.fauzangifari.data.mapper

import com.fauzangifari.data.source.remote.dto.response.ResultUsers
import com.fauzangifari.data.source.remote.dto.response.UsersMeResponse
import com.fauzangifari.data.source.remote.dto.response.UsersResponse
import com.fauzangifari.domain.model.User
import com.fauzangifari.domain.model.UserMe
import com.fauzangifari.domain.model.UserRole

fun UsersMeResponse.toDomain(): UserMe {
    val result = this.result

    val role = when {
        result?.student != null -> UserRole.STUDENT
        result?.teacher != null -> UserRole.TEACHER
        else -> UserRole.UNKNOWN
    }

    return UserMe(
        id = result?.id ?: "",
        email = result?.email ?: "",
        name = result?.name ?: "",
        image = result?.image,
        role = role,
        studentId = result?.student?.id,
        teacherId = result?.teacher?.id,
        student = result?.student?.toDomain(),
        teacher = result?.teacher?.toDomain(),
        secondaryEmail = result?.secondaryEmail
    )
}

fun ResultUsers.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        name = this.name,
        image = this.image,
        secondaryEmail = this.secondaryEmail,
        emailVerified = this.emailVerified
    )
}
