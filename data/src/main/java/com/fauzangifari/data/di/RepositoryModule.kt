package com.fauzangifari.data.di

import com.fauzangifari.data.repository.LetterRepositoryImpl
import com.fauzangifari.data.repository.StudentRepositoryImpl
import com.fauzangifari.domain.repository.LetterRepository
import com.fauzangifari.domain.repository.StudentRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<LetterRepository> {
        LetterRepositoryImpl(apiService = get())
    }

    single<StudentRepository> {
        StudentRepositoryImpl(apiService = get())
    }
}