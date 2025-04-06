package com.fauzangifari.surata.domain.repository

import com.fauzangifari.surata.data.source.remote.response.LetterResponse

interface LetterRepository {
    suspend fun getLetters(): LetterResponse
}