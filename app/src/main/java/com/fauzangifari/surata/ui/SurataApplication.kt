package com.fauzangifari.surata.ui

import android.app.Application
import com.fauzangifari.data.di.networkModule
import com.fauzangifari.data.di.repositoryModule
import com.fauzangifari.domain.di.useCaseModule
import com.fauzangifari.surata.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class SurataApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SurataApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}