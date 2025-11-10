package com.fauzangifari.domain.repository

import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.Presigned
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.model.ReqPresigned

interface LetterRepository {

    suspend fun getLetters(): List<Letter>

    suspend fun getLettersByUserId(userId: String): List<Letter>

    suspend fun getLettersByUserIdFromLocal(): List<Letter>

    suspend fun getLetterById(letterId: String): Letter

    suspend fun getLetterByIdFromLocal(letterId: String): Letter?

    suspend fun postLetter(reqLetter: ReqLetter) : Letter

    suspend fun postPresignedUrl(reqPresigned: ReqPresigned): Presigned

    suspend fun saveLettersToLocal(letters: List<Letter>)

    suspend fun saveLetterToLocal(letter: Letter)

    suspend fun clearLocalCache()
}