package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.UserMe
import com.fauzangifari.domain.repository.UserRepository

class GetUsersMeUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<UserMe> {
        return userRepository.getUsersMe()
    }
}

