package com.fauzangifari.surata.domain.repository

import com.fauzangifari.surata.data.source.remote.response.LetterDetailResponse
import com.fauzangifari.surata.data.source.remote.response.LetterResponse

interface LetterRepository {
    suspend fun getLetters(): LetterResponse

    suspend fun getLetterById(letterId: String): LetterDetailResponse
}