package com.fauzangifari.surata.domain.usecase

import com.fauzangifari.surata.common.Resource
import com.fauzangifari.surata.domain.model.Letter
import com.fauzangifari.surata.domain.repository.LetterRepository
import com.fauzangifari.surata.utils.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDetailLetterUseCase @Inject constructor(
    private val letterRepository: LetterRepository
) {
    operator fun invoke(letterId: String): Flow<Resource<Letter>> = flow {
        try {
            emit(Resource.Loading())
            val response = letterRepository.getLetterById(letterId)
            if (response.success == true && response.result != null) {
                val result = response.result.toDomain()
                emit(Resource.Success(result))
            } else {
                val errorMessage = response.message ?: response.errors?.firstOrNull()?.message
                ?: "Terjadi kesalahan dari server"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan jaringan"))
        } catch (e: IOException) {
            emit(Resource.Error("Tidak dapat menghubungi server. Periksa koneksi internet anda."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga"))
        }
    }
}
