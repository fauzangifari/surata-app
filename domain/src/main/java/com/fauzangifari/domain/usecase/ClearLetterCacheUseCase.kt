package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.repository.LetterRepository

class ClearLetterCacheUseCase(
    private val letterRepository: LetterRepository
) {
    suspend operator fun invoke() {
        letterRepository.clearLocalCache()
    }
}

