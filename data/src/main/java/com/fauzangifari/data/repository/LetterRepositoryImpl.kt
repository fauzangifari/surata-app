package com.fauzangifari.data.repository

import com.fauzangifari.data.source.remote.retrofit.ApiService
import com.fauzangifari.data.utils.toDomain
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.repository.LetterRepository

class LetterRepositoryImpl(
    private val apiService: ApiService
) : LetterRepository {

    override suspend fun getLetters(): List<Letter> {
        val response = apiService.getLetters()
        return response.result?.mapNotNull { it?.toDomain() } ?: emptyList()
    }

    override suspend fun getLetterById(letterId: String): Letter {
        val response = apiService.getLetterById(letterId)
        return response.result?.toDomain() ?: throw Exception("Letter not found")
    }
}