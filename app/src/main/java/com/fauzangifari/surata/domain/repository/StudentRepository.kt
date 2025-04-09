package com.fauzangifari.surata.domain.repository

import com.fauzangifari.surata.data.source.remote.response.StudentResponse

interface StudentRepository {
    suspend fun getStudents(): StudentResponse
}