package com.fauzangifari.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class AuthPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val USER_ID_KEY = stringPreferencesKey("user_id_key")
        private val USER_NAME_KEY = stringPreferencesKey("user_name_key")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email_key")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        val prefs = dataStore.data.first()
        return prefs[TOKEN_KEY]
    }

    val token: Flow<String?> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            prefs[TOKEN_KEY]
        }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
        }
    }

    suspend fun getUserId(): String? {
        val prefs = dataStore.data.first()
        return prefs[USER_ID_KEY]
    }

    val userId: Flow<String?> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            prefs[USER_ID_KEY]
        }

    suspend fun saveUserName(name: String) {
        dataStore.edit { prefs ->
            prefs[USER_NAME_KEY] = name
        }
    }

    suspend fun getUserName(): String? {
        val prefs = dataStore.data.first()
        return prefs[USER_NAME_KEY]
    }

    val userName: Flow<String?> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            prefs[USER_NAME_KEY]
        }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit { prefs ->
            prefs[USER_EMAIL_KEY] = email
        }
    }

    suspend fun getUserEmail(): String? {
        val prefs = dataStore.data.first()
        return prefs[USER_EMAIL_KEY]
    }

    val userEmail: Flow<String?> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            prefs[USER_EMAIL_KEY]
        }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
