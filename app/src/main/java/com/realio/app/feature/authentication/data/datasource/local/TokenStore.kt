package com.realio.app.feature.authentication.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface TokenStorage {
    suspend fun saveTokens(authToken: String, refreshToken: String)
    suspend fun getAuthToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
}

class TokenStorageImpl(private val dataStore: DataStore<Preferences>) : TokenStorage {
    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveTokens(authToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = authToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun getAuthToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN]
        }.firstOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }.firstOrNull()
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }
}