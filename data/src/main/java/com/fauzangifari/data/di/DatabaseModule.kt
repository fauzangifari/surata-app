package com.fauzangifari.data.di

import androidx.room.Room
import com.fauzangifari.data.source.local.room.database.Database
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule =  module {

    single {
        Room.databaseBuilder(
                androidContext(),
                Database::class.java,
                "letter_db"
            ).fallbackToDestructiveMigration(false).build()
    }

    factory {
        get<Database>().letterDao()
    }

}