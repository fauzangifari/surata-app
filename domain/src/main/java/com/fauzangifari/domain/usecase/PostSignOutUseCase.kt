package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.repository.AuthRepository

open class PostSignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        success: Boolean
    ): Resource<Boolean> {
        return try {
            authRepository.signOut(
                success = success
            )
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}