package com.fauzangifari.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.fauzangifari.data.source.local.room.entity.LetterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LetterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetter(letter: LetterEntity)

    @Transaction
    @Query("SELECT * FROM letters")
    suspend fun getAllLetters(): List<LetterEntity>

}