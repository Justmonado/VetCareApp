package com.pajasoft.vetcare.Data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "vetcare_prefs")

class TokenManager(private val context: Context) {
    @Volatile
    var cachedToken: String? = null
        private set

    val tokenFlow: Flow<String?> =
        context.dataStore.data.map { it[KEY_TOKEN] }

    suspend fun load(): String? {
        cachedToken = context.dataStore.data.first()[KEY_TOKEN]
        return cachedToken
    }

    suspend fun saveToken(token: String) {
        cachedToken = token
        context.dataStore.edit { it[KEY_TOKEN] = token }
    }

    suspend fun clear() {
        cachedToken = null
        context.dataStore.edit { it.remove(KEY_TOKEN) }
    }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("jwt_token")
    }
}