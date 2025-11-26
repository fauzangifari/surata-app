package com.fauzangifari.domain.usecase

import android.util.Log
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.User
import com.fauzangifari.domain.repository.UserRepository

class GetAllUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Resource<List<User>> {
        return repository.getAllUsers()
    }
}
