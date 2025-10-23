package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class PostLetterUseCase(
    private val letterRepository: LetterRepository
) {
    operator fun invoke(reqLetter: ReqLetter): Flow<Resource<Letter>> = flow {
        try {
            emit(Resource.Loading())
            val safeReq = reqLetter.copy(
                beginDate = reqLetter.beginDate?.takeIf { it.isNotBlank() },
                endDate = reqLetter.endDate?.takeIf { it.isNotBlank() },
            )
            val response = letterRepository.postLetter(safeReq)
            emit(Resource.Success(response))
        } catch (e: IOException) {
            emit(Resource.Error("Tidak dapat terhubung ke server. Periksa koneksi internet."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Gagal memproses surat."))
        }
    }
}
