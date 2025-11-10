package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class GetLetterByUserIdUseCase (
    private val letterRepository: LetterRepository
){
    operator fun invoke(userId: String): Flow<Resource<List<Letter>>> = flow {
        try {
            emit(Resource.Loading())

            val localLetters = letterRepository.getLettersByUserIdFromLocal()
            if (localLetters.isNotEmpty()) {
                emit(Resource.Success(localLetters))
            }

            try {
                val remoteLetters = letterRepository.getLettersByUserId(userId)
                letterRepository.saveLettersToLocal(remoteLetters)
                emit(Resource.Success(remoteLetters))
            } catch (e: IOException) {
                if (localLetters.isNotEmpty()) {
                    emit(Resource.Success(localLetters))
                } else {
                    emit(Resource.Error("Tidak dapat menghubungi server. Periksa koneksi internet anda."))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga"))
        }
    }
}