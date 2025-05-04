package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class GetDetailLetterUseCase (
    private val letterRepository: LetterRepository
) {
    operator fun invoke(letterId: String): Flow<Resource<Letter>> = flow {
        try {
            emit(Resource.Loading())
            val response = letterRepository.getLetterById(letterId)
            val letterList = response
            emit(Resource.Success(letterList))
        } catch (e: IOException) {
            emit(Resource.Error("Tidak dapat menghubungi server. Periksa koneksi internet anda."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga"))
        }
    }
}
