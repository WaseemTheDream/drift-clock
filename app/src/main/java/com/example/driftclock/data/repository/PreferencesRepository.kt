package com.example.driftclock.data.repository

import com.example.driftclock.data.datastore.UserPreferencesDataStore
import com.example.driftclock.data.model.Profile
import com.example.driftclock.data.model.Profiles
import com.example.driftclock.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(
    private val dataStore: UserPreferencesDataStore
) {
    val userPreferences: Flow<UserPreferences> = dataStore.userPreferences

    val currentProfile: Flow<Profile> = userPreferences.map { prefs ->
        val profile = Profiles.getById(prefs.selectedProfileId)
        if (profile.isCustom) {
            profile.copy(speedMultiplier = prefs.customSpeedMultiplier)
        } else {
            profile
        }
    }

    val currentSpeedMultiplier: Flow<Float> = userPreferences.map { prefs ->
        val profile = Profiles.getById(prefs.selectedProfileId)
        if (profile.isCustom) {
            prefs.customSpeedMultiplier
        } else {
            profile.speedMultiplier
        }
    }

    suspend fun selectProfile(profileId: String) {
        dataStore.setSelectedProfile(profileId)
    }

    suspend fun setCustomSpeed(multiplier: Float) {
        dataStore.setCustomSpeedMultiplier(multiplier.coerceIn(0.5f, 2.0f))
    }

    suspend fun completeOnboarding() {
        dataStore.setOnboardingCompleted()
    }

    suspend fun resetToDefaults() {
        dataStore.resetPreferences()
    }
}
