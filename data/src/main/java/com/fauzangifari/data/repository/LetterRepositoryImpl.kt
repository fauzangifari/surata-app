package com.fauzangifari.data.repository

import com.fauzangifari.data.source.remote.retrofit.LetterApiService
import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.repository.LetterRepository

class LetterRepositoryImpl(
    private val letterApiService: LetterApiService
) : LetterRepository {

    override suspend fun getLetters(): List<Letter> {
        val response = letterApiService.getLetters()
        return response.result?.mapNotNull { it?.toDomain() } ?: emptyList()
    }

    override suspend fun getLetterById(letterId: String): Letter {
        val response = letterApiService.getLetterById(letterId)
        return response.result?.toDomain() ?: throw Exception("Letter not found")
    }
}