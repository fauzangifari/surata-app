package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Session
import com.fauzangifari.domain.repository.AuthRepository

class GetSessionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : Resource<Session> {
        return authRepository.getSession()
    }
}