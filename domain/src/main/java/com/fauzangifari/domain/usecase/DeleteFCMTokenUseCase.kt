package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.repository.FCMRepository

class DeleteFCMTokenUseCase(
    private val fcmRepository: FCMRepository
) {
    suspend operator fun invoke(tokenId: String): Resource<Unit> {
        return fcmRepository.deleteToken(tokenId)
    }
}

