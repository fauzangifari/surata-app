package com.fauzangifari.data.source.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fauzangifari.data.source.local.room.dao.LetterDao
import com.fauzangifari.data.source.local.room.entity.LetterEntity

@Database(
    entities = [LetterEntity::class],
    version = 1, exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun letterDao(): LetterDao
}