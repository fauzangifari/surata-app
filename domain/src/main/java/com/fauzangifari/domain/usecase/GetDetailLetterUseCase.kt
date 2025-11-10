package com.fauzangifari.domain.usecase

import android.util.Log
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

            // 1. Ambil data dari local terlebih dahulu
            val localLetter = letterRepository.getLetterByIdFromLocal(letterId)
            if (localLetter != null) {
                emit(Resource.Success(localLetter))
            }

            // 2. Fetch dari remote dan update local
            try {
                val remoteLetter = letterRepository.getLetterById(letterId)
                letterRepository.saveLetterToLocal(remoteLetter)
                Log.d("GetDetailLetterUseCase", "Response: $remoteLetter")
                emit(Resource.Success(remoteLetter))
            } catch (e: IOException) {
                // Jika gagal fetch remote tapi ada data local, tetap berhasil
                if (localLetter != null) {
                    emit(Resource.Success(localLetter))
                } else {
                    emit(Resource.Error("Tidak dapat menghubungi server. Periksa koneksi internet anda."))
                }
            }
        } catch (e: Exception) {
            Log.e("GetDetailLetterUseCase", "Error: ${e.message}")
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga"))
        }
    }
}
