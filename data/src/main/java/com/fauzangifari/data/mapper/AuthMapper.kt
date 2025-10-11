package com.fauzangifari.data.mapper

import com.fauzangifari.data.source.remote.dto.request.SignInRequest
import com.fauzangifari.data.source.remote.dto.request.SignOutRequest
import com.fauzangifari.data.source.remote.dto.response.SessionResponse
import com.fauzangifari.data.source.remote.dto.response.SignInResponse
import com.fauzangifari.domain.model.Auth
import com.fauzangifari.domain.model.Session
import com.fauzangifari.domain.model.SignInParam
import com.fauzangifari.domain.model.User

fun SignInParam.toRequest(): SignInRequest {
    return SignInRequest(
        email = this.email,
        password = this.password,
        callBackURL = null,
        rememberMe = if (this.rememberMe) "true" else "false"
    )
}

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

fun SignOutRequest.toDomain(): Boolean {
    return this.success
}

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



