package com.fauzangifari.domain.di

import com.fauzangifari.domain.usecase.ClearLetterCacheUseCase
import com.fauzangifari.domain.usecase.GetDetailLetterUseCase
import com.fauzangifari.domain.usecase.GetLetterByUserIdUseCase
import com.fauzangifari.domain.usecase.GetLetterLocalUseCase
import com.fauzangifari.domain.usecase.GetLetterUseCase
import com.fauzangifari.domain.usecase.GetSessionUseCase
import com.fauzangifari.domain.usecase.GetStudentUseCase
import com.fauzangifari.domain.usecase.PostLetterUseCase
import com.fauzangifari.domain.usecase.PostPresignedUrlUseCase
import com.fauzangifari.domain.usecase.PostSignInUseCase
import com.fauzangifari.domain.usecase.PostSignOutUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Get
    factory { GetDetailLetterUseCase(get()) }
    factory { GetLetterByUserIdUseCase(get()) }
    factory { GetLetterUseCase(get()) }
    factory { GetSessionUseCase(get()) }
    factory { GetLetterLocalUseCase(get()) }
    factory { GetStudentUseCase(get()) }

    // Post
    factory { PostSignInUseCase(get()) }
    factory { PostSignOutUseCase(get()) }
    factory { PostLetterUseCase(get()) }
    factory { PostPresignedUrlUseCase(get()) }

    // Clear
    factory { ClearLetterCacheUseCase(get()) }

}