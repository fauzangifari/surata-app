package com.fauzangifari.domain.repository

import com.fauzangifari.domain.model.Letter

interface LetterLocalRepository {

    suspend fun getAllLetter(): List<Letter>

    suspend fun insertLetter(letter: Letter)
}