package com.fauzangifari.surata.data.repository

import com.fauzangifari.surata.data.source.remote.response.LetterResponse
import com.fauzangifari.surata.data.source.remote.retrofit.ApiService
import com.fauzangifari.surata.domain.repository.LetterRepository
import javax.inject.Inject

class LetterRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : LetterRepository {
    override suspend fun getLetters(): LetterResponse {
        return apiService.getLetters()
    }
}