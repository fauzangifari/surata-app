package com.fauzangifari.data.repository

import com.fauzangifari.data.source.remote.retrofit.StudentApiService
import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.domain.model.Student
import com.fauzangifari.domain.repository.StudentRepository

class StudentRepositoryImpl(
    private val studentApiService: StudentApiService
) : StudentRepository{

    override suspend fun getStudents(): List<Student> {
        val response = studentApiService.getAllStudent()
        return response.result?.mapNotNull { it?.toDomain() } ?: emptyList()
    }

}