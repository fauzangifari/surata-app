package com.fauzangifari.domain.di

import com.fauzangifari.domain.usecase.GetDetailLetterUseCase
import com.fauzangifari.domain.usecase.GetLetterUseCase
import com.fauzangifari.domain.usecase.GetSessionUseCase
import com.fauzangifari.domain.usecase.PostSignInUseCase
import com.fauzangifari.domain.usecase.PostSignOutUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Get
    factory { GetDetailLetterUseCase(get()) }
    factory { GetLetterUseCase(get()) }
    factory { GetSessionUseCase(get()) }

    // Post
    factory { PostSignInUseCase(get()) }
    factory { PostSignOutUseCase(get()) }

}