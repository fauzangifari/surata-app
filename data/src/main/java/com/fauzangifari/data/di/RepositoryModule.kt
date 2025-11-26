package com.fauzangifari.data.di

import com.fauzangifari.data.repository.AuthRepositoryImpl
import com.fauzangifari.data.repository.FCMRepositoryImpl
import com.fauzangifari.data.repository.LetterRepositoryImpl
import com.fauzangifari.data.repository.NotificationRepositoryImpl
import com.fauzangifari.data.repository.StudentRepositoryImpl
import com.fauzangifari.data.repository.UserRepositoryImpl
import com.fauzangifari.domain.repository.AuthRepository
import com.fauzangifari.domain.repository.FCMRepository
import com.fauzangifari.domain.repository.LetterRepository
import com.fauzangifari.domain.repository.NotificationRepository
import com.fauzangifari.domain.repository.StudentRepository
import com.fauzangifari.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<LetterRepository> {
        LetterRepositoryImpl(letterApiService = get(), letterDao = get())
    }

    single<StudentRepository> {
        StudentRepositoryImpl(studentApiService = get())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(authApiService = get(), authPreferences = get(), letterDao = get(), notificationDao = get())
    }

    single<UserRepository> {
        UserRepositoryImpl(userApiService = get())
    }

    single<NotificationRepository> {
        NotificationRepositoryImpl(notificationDao = get())
    }

    single<FCMRepository> {
        FCMRepositoryImpl(fcmApiService = get(), authPreferences = get())
    }
}