package com.fauzangifari.domain.repository

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.User
import com.fauzangifari.domain.model.UserMe

interface UserRepository {
    suspend fun getUsersMe(): Resource<UserMe>

    suspend fun getAllUsers(): Resource<List<User>>
}

