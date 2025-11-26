package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.repository.FCMRepository

class SaveFCMTokenUseCase(
    private val fcmRepository: FCMRepository
) {
    suspend operator fun invoke(token: String): Resource<String> {
        return fcmRepository.saveToken(token)
    }
}