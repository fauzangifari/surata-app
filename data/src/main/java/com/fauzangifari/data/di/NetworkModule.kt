package com.fauzangifari.data.di

import com.fauzangifari.domain.common.Constant
import com.fauzangifari.data.source.remote.retrofit.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single { Constant.BASE_URL }

    single {
        Retrofit.Builder()
            .baseUrl(get<String>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}