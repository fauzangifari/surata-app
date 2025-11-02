package com.fauzangifari.domain.repository

import com.fauzangifari.domain.model.Student

interface StudentRepository {

    suspend fun getStudents(): List<Student>

}