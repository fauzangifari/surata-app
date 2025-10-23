package com.fauzangifari.domain.repository

import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.ReqLetter

interface LetterRepository {

    suspend fun getLetters(): List<Letter>

    suspend fun getLettersByUserId(userId: String): List<Letter>

    suspend fun getLetterById(letterId: String): Letter

    suspend fun postLetter(reqLetter: ReqLetter) : Letter
}