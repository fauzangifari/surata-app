package com.fauzangifari.domain.usecase

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Student
import com.fauzangifari.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class GetStudentUseCase(
    private val studentRepository: StudentRepository
) {
    operator fun invoke(): Flow<Resource<List<Student>>> = flow {
        try {
            emit(Resource.Loading())
            val response = studentRepository.getStudents()
            val studentList = response
            emit(Resource.Success(studentList))
        } catch (e: IOException) {
            emit(Resource.Error("Tidak dapat menghubungi server. Periksa koneksi internet anda."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga"))
        }
    }
}