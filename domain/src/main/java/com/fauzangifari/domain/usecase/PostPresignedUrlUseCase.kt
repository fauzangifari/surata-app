package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Presigned
import com.fauzangifari.domain.model.ReqPresigned
import com.fauzangifari.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostPresignedUrlUseCase(
    private val letterRepository: LetterRepository
) {
    operator fun invoke(reqPresigned: ReqPresigned): Flow<Resource<Presigned>> = flow {
        try {
            emit(Resource.Loading())
            val response = letterRepository.postPresignedUrl(reqPresigned)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Gagal mendapatkan presigned URL."))
        }
    }
}