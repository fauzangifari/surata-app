package com.fauzangifari.domain.usecase

import android.util.Log
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
            val response = letterRepository.getLettersByUserId(userId)
            val letterList = response
            emit(Resource.Success(letterList))
        } catch (e: IOException) {
            emit(Resource.Error("Tidak dapat menghubungi server. Periksa koneksi internet anda."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga"))
        }
    }
}