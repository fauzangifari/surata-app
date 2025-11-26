package com.fauzangifari.domain.repository

import com.fauzangifari.domain.common.Resource

interface FCMRepository {
    /**
     * Save FCM token to backend server
     */
    suspend fun saveToken(token: String): Resource<String>

    /**
     * Delete FCM token from backend server
     */
    suspend fun deleteToken(tokenId: String): Resource<Unit>
}
