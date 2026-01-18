package com.example.driftclock.data.model

data class Profile(
    val id: String,
    val name: String,
    val description: String,
    val speedMultiplier: Float,
    val isCustom: Boolean = false
)

object Profiles {
    val TURBO = Profile(
        id = "turbo",
        name = "Turbo",
        description = "Rest periods fly by. That 60-second break? Done in 48.",
        speedMultiplier = 1.25f
    )

    val GRIND = Profile(
        id = "grind",
        name = "Grind",
        description = "Make every second burn. Your 30-second plank just became 36.",
        speedMultiplier = 0.8f
    )

    val STEALTH = Profile(
        id = "stealth",
        name = "Stealth",
        description = "Subtle enough to fool yourself. Perfect for pushing through study sessions.",
        speedMultiplier = 1.1f
    )

    val TRICKSTER = Profile(
        id = "trickster",
        name = "Trickster",
        description = "For party bets and bar tricks. 'Watch me hold my breath for 2 minutes.'",
        speedMultiplier = 1.5f
    )

    val CUSTOM = Profile(
        id = "custom",
        name = "Custom",
        description = "Dial in your own time distortion. Fine-tune to the decimal.",
        speedMultiplier = 1.0f,
        isCustom = true
    )

    val ALL = listOf(TURBO, GRIND, STEALTH, TRICKSTER, CUSTOM)

    fun getById(id: String): Profile = ALL.find { it.id == id } ?: STEALTH
}
