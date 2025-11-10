package com.fauzangifari.surata.ui

import android.app.Application
import com.fauzangifari.data.di.databaseModule
import com.fauzangifari.data.di.networkModule
import com.fauzangifari.data.di.preferencesModule
import com.fauzangifari.data.di.repositoryModule
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.data.utils.AuthTokenProvider
import com.fauzangifari.domain.di.useCaseModule
import com.fauzangifari.surata.di.viewModelModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject

class SurataApplication : Application() {

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
                    viewModelModule,
                    preferencesModule,
                    databaseModule
                )
            )
        }

        val authPreferences: AuthPreferences by inject(AuthPreferences::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val savedToken = authPreferences.getToken()
            AuthTokenProvider.setToken(savedToken)
        }
    }
}
