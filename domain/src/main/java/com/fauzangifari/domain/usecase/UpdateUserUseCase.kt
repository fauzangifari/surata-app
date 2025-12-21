package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.UserMe
import com.fauzangifari.domain.repository.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        usersId: String,
        name: String?,
        secondaryEmail: String?
    ): Resource<UserMe> {
        return userRepository.updateUser(usersId, name, secondaryEmail)
    }
}

