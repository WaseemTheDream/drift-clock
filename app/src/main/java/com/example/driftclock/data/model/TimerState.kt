package com.example.driftclock.data.model

data class TimerState(
    val initialDurationMs: Long = 0L,
    val displayedRemainingMs: Long = 0L,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val isPaused: Boolean = false
) {
    val isActive: Boolean
        get() = isRunning || isPaused

    val progress: Float
        get() = if (initialDurationMs > 0) {
            (displayedRemainingMs.toFloat() / initialDurationMs.toFloat()).coerceIn(0f, 1f)
        } else 0f
}
