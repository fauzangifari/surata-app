package com.fauzangifari.data.repository

import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.data.mapper.toEntity
import com.fauzangifari.data.source.local.room.dao.LetterDao
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.repository.LetterLocalRepository

class LetterRepositoryLocalImpl(
    private val letterDao: LetterDao
) : LetterLocalRepository{

    override suspend fun getAllLetter(): List<Letter> {
        return letterDao.getAllLetters().map { it.toDomain() }
    }

    override suspend fun insertLetter(letter: Letter) {
        letterDao.insertLetter(letter = letter.toEntity())
    }

}