package com.fauzangifari.data.di

import com.fauzangifari.data.source.remote.retrofit.AuthApiService
import com.fauzangifari.data.source.remote.retrofit.AuthInterceptor
import com.fauzangifari.data.source.remote.retrofit.LetterApiService
import com.fauzangifari.domain.common.Constant
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single { AuthInterceptor(get()) }

    single(named("letterRetrofit")) {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single(named("authRetrofit")) {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_DEV)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(qualifier = named("letterRetrofit"))
            .create(LetterApiService::class.java)
    }

    single {
        get<Retrofit>(qualifier = named("authRetrofit"))
            .create(AuthApiService::class.java)
    }
}
