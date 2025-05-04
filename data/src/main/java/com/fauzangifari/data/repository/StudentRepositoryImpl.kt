package com.fauzangifari.data.repository

import com.fauzangifari.data.source.remote.retrofit.ApiService
import com.fauzangifari.domain.repository.StudentRepository

class StudentRepositoryImpl(
    private val apiService: ApiService
) : StudentRepository{

}