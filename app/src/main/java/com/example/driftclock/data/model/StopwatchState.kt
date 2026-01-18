package com.example.driftclock.data.model

data class LapTime(
    val lapNumber: Int,
    val lapTimeMs: Long,
    val totalTimeMs: Long
)

data class StopwatchState(
    val displayedElapsedMs: Long = 0L,
    val isRunning: Boolean = false,
    val laps: List<LapTime> = emptyList()
) {
    val isActive: Boolean
        get() = isRunning || displayedElapsedMs > 0
}
