package com.fauzangifari.domain.repository

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Auth
import com.fauzangifari.domain.model.Session

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    ) : Resource<Auth>

    suspend fun signOut() : Resource<Boolean>

    suspend fun getSession() : Resource<Session>

}