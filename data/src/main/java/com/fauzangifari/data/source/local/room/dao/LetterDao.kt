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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetters(letters: List<LetterEntity>)

    @Transaction
    @Query("SELECT * FROM letters ORDER BY createdAt DESC")
    suspend fun getAllLetters(): List<LetterEntity>

    @Query("SELECT * FROM letters WHERE id = :letterId LIMIT 1")
    suspend fun getLetterById(letterId: String): LetterEntity?

    @Query("DELETE FROM letters")
    suspend fun deleteAllLetters()

    @Query("DELETE FROM letters WHERE id = :letterId")
    suspend fun deleteLetterById(letterId: String)
}