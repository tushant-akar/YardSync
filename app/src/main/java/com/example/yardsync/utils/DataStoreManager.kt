package com.example.yardsync.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
        val EMPLOYEE_EMAIL = stringPreferencesKey("employeeEmail")
    }

    suspend fun saveEmployeeEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMPLOYEE_EMAIL] = email
        }
    }

    fun getEmployeeEmail(): Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[EMPLOYEE_EMAIL]
            }
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]
            }
    }

    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}