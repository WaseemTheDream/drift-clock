package com.example.driftclock.data.model

data class UserPreferences(
    val selectedProfileId: String = "stealth",
    val customSpeedMultiplier: Float = 1.0f,
    val hasCompletedOnboarding: Boolean = false
)
