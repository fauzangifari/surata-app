package com.fauzangifari.data.repository

import android.util.Log
import com.fauzangifari.data.source.remote.retrofit.LetterApiService
import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.data.mapper.toRequest
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.repository.LetterRepository

class LetterRepositoryImpl(
    private val letterApiService: LetterApiService
) : LetterRepository {

    override suspend fun getLetters(): List<Letter> {
        val response = letterApiService.getLetters()
        return response.result?.mapNotNull { it?.toDomain() } ?: emptyList()
    }

    override suspend fun getLettersByUserId(userId: String): List<Letter> {
        val response = letterApiService.getLettersByUserId(userId)
        Log.d("LetterRepositoryImpl", "Fetched letters for userId $userId: ${response.result}")
        return response.result?.mapNotNull { it?.toDomain() } ?: emptyList()
    }

    override suspend fun getLetterById(letterId: String): Letter {
        val response = letterApiService.getLetterById(letterId)
        return response.result?.toDomain() ?: throw Exception("Gagal memproses surat")
    }

    override suspend fun postLetter(reqLetter: ReqLetter): Letter {
        return try {
            val request = reqLetter.toRequest()
            val response = letterApiService.postLetter(request)
            response.result?.toDomain() ?: throw Exception("Gagal memproses surat")
        } catch (e: Exception) {
            throw Exception("Gagal memproses surat: ${e.message}")
        }
    }

}