package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

open class PatchLetterUseCase(
    private val letterRepository: LetterRepository
) {
    open operator fun invoke(letterId: String, reqLetter: ReqLetter): Flow<Resource<Letter>> = flow {
        try {
            emit(Resource.Loading())
            val safeReq = reqLetter.copy(
                beginDate = reqLetter.beginDate?.takeIf { it.isNotBlank() },
                endDate = reqLetter.endDate?.takeIf { it.isNotBlank() },
            )
            val response = letterRepository.patchLetter(letterId, safeReq)
            emit(Resource.Success(response))
        } catch (e: IOException) {
            emit(Resource.Error("Tidak dapat terhubung ke server. Periksa koneksi internet."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Gagal memperbarui surat."))
        }
    }
}
