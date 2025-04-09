package com.fauzangifari.surata.data.repository

import com.fauzangifari.surata.data.source.remote.response.StudentResponse
import com.fauzangifari.surata.data.source.remote.retrofit.ApiService
import com.fauzangifari.surata.domain.repository.StudentRepository
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : StudentRepository{

    override suspend fun getStudents(): StudentResponse {
        return apiService.getStudents()
    }
}