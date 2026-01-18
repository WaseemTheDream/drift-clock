package com.example.driftclock.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.driftclock.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "drift_clock_preferences")

class UserPreferencesDataStore(private val context: Context) {

    private object PreferencesKeys {
        val SELECTED_PROFILE_ID = stringPreferencesKey("selected_profile_id")
        val CUSTOM_SPEED_MULTIPLIER = floatPreferencesKey("custom_speed_multiplier")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        UserPreferences(
            selectedProfileId = preferences[PreferencesKeys.SELECTED_PROFILE_ID] ?: "stealth",
            customSpeedMultiplier = preferences[PreferencesKeys.CUSTOM_SPEED_MULTIPLIER] ?: 1.0f,
            hasCompletedOnboarding = preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false
        )
    }

    suspend fun setSelectedProfile(profileId: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_PROFILE_ID] = profileId
        }
    }

    suspend fun setCustomSpeedMultiplier(multiplier: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_SPEED_MULTIPLIER] = multiplier
        }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] = true
        }
    }

    suspend fun resetPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
