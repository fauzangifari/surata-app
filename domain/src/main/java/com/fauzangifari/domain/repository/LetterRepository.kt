package com.fauzangifari.domain.repository

import com.fauzangifari.domain.model.Letter


interface LetterRepository {
    suspend fun getLetters(): List<Letter>

    suspend fun getLetterById(letterId: String): Letter
}