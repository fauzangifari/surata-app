package com.fauzangifari.data.mapper

import com.fauzangifari.data.source.remote.dto.response.SessionResponse
import com.fauzangifari.data.source.remote.dto.response.SignInResponse
import com.fauzangifari.domain.model.Auth
import com.fauzangifari.domain.model.Session
import com.fauzangifari.domain.model.User

fun SignInResponse.toDomain() = Auth(
    token = token,
    user = User(
        id = user.id,
        email = user.email,
        name = user.name,
        image = user.image,
        emailVerified = user.emailVerified,
    )
)

fun SessionResponse.toDomain(): Session {
    return Session(
        id = session?.id.orEmpty(),
        expiresAt = session?.expiresAt.orEmpty(),
        token = session?.token.orEmpty(),
        ipAddress = session?.ipAddress.orEmpty(),
        userAgent = session?.userAgent.orEmpty(),
        userId = session?.userId.orEmpty(),
        user = User(
            id = user?.id.orEmpty(),
            name = user?.name.orEmpty(),
            email = user?.email.orEmpty(),
            emailVerified = user?.emailVerified == true,
            image = user?.image.orEmpty()
        )
    )
}



