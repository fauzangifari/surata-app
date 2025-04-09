package com.fauzangifari.surata.di

import com.fauzangifari.surata.data.repository.LetterRepositoryImpl
import com.fauzangifari.surata.data.repository.StudentRepositoryImpl
import com.fauzangifari.surata.data.source.remote.retrofit.ApiService
import com.fauzangifari.surata.domain.repository.LetterRepository
import com.fauzangifari.surata.domain.repository.StudentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLetterRepository(
        apiService: ApiService
    ) : LetterRepository = LetterRepositoryImpl(apiService = apiService)

    @Singleton
    @Provides
    fun provideStudentRepository(
        apiService: ApiService
    ) : StudentRepository = StudentRepositoryImpl(apiService = apiService)
}