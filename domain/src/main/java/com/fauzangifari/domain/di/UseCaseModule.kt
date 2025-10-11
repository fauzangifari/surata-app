package com.fauzangifari.domain.di

import com.fauzangifari.domain.usecase.GetDetailLetterUseCase
import com.fauzangifari.domain.usecase.GetLetterUseCase
import com.fauzangifari.domain.usecase.GetSessionUseCase
import com.fauzangifari.domain.usecase.PostSignInUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { GetDetailLetterUseCase(get()) }
    factory { GetLetterUseCase(get()) }
    factory { PostSignInUseCase(get()) }
    factory { GetSessionUseCase(get()) }

}