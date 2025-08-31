package com.example.sociallearningapp.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DS_NAME = "app_prefs"
private val Context.dataStore by preferencesDataStore(name = DS_NAME)

object AppPreferences {
    private val KEY_ONBOARDED = booleanPreferencesKey("onboarded")

    fun onboardedFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[KEY_ONBOARDED] ?: false }

    suspend fun setOnboarded(context: Context, value: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_ONBOARDED] = value }
    }
}
