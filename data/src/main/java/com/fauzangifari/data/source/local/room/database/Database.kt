package com.fauzangifari.data.source.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fauzangifari.data.source.local.room.dao.LetterDao
import com.fauzangifari.data.source.local.room.dao.NotificationDao
import com.fauzangifari.data.source.local.room.entity.LetterEntity
import com.fauzangifari.data.source.local.room.entity.NotificationEntity

@Database(
    entities = [LetterEntity::class, NotificationEntity::class],
    version = 2, exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun letterDao(): LetterDao
    abstract fun notificationDao(): NotificationDao
}