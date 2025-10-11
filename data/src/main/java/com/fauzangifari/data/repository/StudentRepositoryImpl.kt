package com.fauzangifari.data.repository

import com.fauzangifari.data.source.remote.retrofit.LetterApiService
import com.fauzangifari.domain.repository.StudentRepository

class StudentRepositoryImpl(
    private val letterApiService: LetterApiService
) : StudentRepository{

}