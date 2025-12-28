package com.fauzangifari.data.repository

import com.fauzangifari.data.source.local.room.dao.LetterDao
import com.fauzangifari.data.source.remote.retrofit.LetterApiService
import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.data.mapper.toEntity
import com.fauzangifari.data.mapper.toRequest
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.Presigned
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.model.ReqPresigned
import com.fauzangifari.domain.repository.LetterRepository
import retrofit2.HttpException

class LetterRepositoryImpl(
    private val letterApiService: LetterApiService,
    private val letterDao: LetterDao
) : LetterRepository {

    override suspend fun getLetters(): List<Letter> {
        val response = letterApiService.getLetters()
        return response.result?.mapNotNull { it?.toDomain() } ?: emptyList()
    }

    override suspend fun getLettersByUserId(userId: String): List<Letter> {
        val response = letterApiService.getLettersByUserId(userId)
        return response.result?.mapNotNull { it?.toDomain() } ?: emptyList()
    }

    override suspend fun getLettersByUserIdFromLocal(): List<Letter> {
        return letterDao.getAllLetters().map { it.toDomain() }
    }

    override suspend fun getLetterById(letterId: String): Letter {
        val response = letterApiService.getLetterById(letterId)
        val letter = response.result?.toDomain() ?: throw Exception("Gagal memproses surat")

        // Debug logging
        android.util.Log.d("LetterRepository", "Letter ID: ${letter.id}, Type: ${letter.letterType}")
        android.util.Log.d("LetterRepository", "History count: ${letter.history.size}")
        letter.history.forEachIndexed { index, history ->
            android.util.Log.d("LetterRepository", "History[$index]: ${history.status} - ${history.actorName} - ${history.timestamp}")
        }

        return letter
    }

    override suspend fun getLetterByIdFromLocal(letterId: String): Letter? {
        return letterDao.getLetterById(letterId)?.toDomain()
    }

    override suspend fun postLetter(reqLetter: ReqLetter): Letter {
        return try {
            val request = reqLetter.toRequest()
            val response = letterApiService.postLetter(request)
            response.result?.toDomain() ?: throw Exception("Gagal memproses surat")
        } catch (e: HttpException) {
            when(e.code()) {
                400 -> throw Exception("Gagal memproses surat")
            }
            throw Exception("Gagal memproses surat: ${e.message}")
        }
    }

    override suspend fun patchLetter(letterId: String, reqLetter: ReqLetter): Letter {
        return try {
            val request = reqLetter.toRequest()
            val response = letterApiService.patchLetter(letterId, request)
            response.result?.toDomain() ?: throw Exception("Gagal memperbarui surat")
        } catch (e: HttpException) {
            when(e.code()) {
                400 -> throw Exception("Gagal memperbarui surat")
                404 -> throw Exception("Surat tidak ditemukan")
            }
            throw Exception("Gagal memperbarui surat: ${e.message}")
        }
    }

    override suspend fun resubmitLetter(letterId: String): Letter {
        return try {
            val response = letterApiService.resubmitLetter(letterId)
            response.result?.toDomain() ?: throw Exception("Gagal mengirim ulang surat")
        } catch (e: HttpException) {
            when(e.code()) {
                400 -> throw Exception("Gagal mengirim ulang surat")
                404 -> throw Exception("Surat tidak ditemukan")
            }
            throw Exception("Gagal mengirim ulang surat: ${e.message}")
        }
    }

    override suspend fun postPresignedUrl(reqPresigned: ReqPresigned): Presigned {
        return try {
            val request = reqPresigned.toRequest()
            val response = letterApiService.postPresignedUrl(request)
            response.toDomain() ?: throw Exception("Gagal mendapatkan presigned URL")
        } catch (e: Exception) {
            throw Exception("Gagal mendapatkan presigned URL: ${e.message}")
        }
    }

    override suspend fun saveLettersToLocal(letters: List<Letter>) {
        letterDao.insertLetters(letters.map { it.toEntity() })
    }

    override suspend fun saveLetterToLocal(letter: Letter) {
        letterDao.insertLetter(letter.toEntity())
    }

    override suspend fun clearLocalCache() {
        letterDao.deleteAllLetters()
    }

}