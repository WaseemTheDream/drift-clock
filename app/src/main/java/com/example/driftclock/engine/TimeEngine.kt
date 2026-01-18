package com.example.driftclock.engine

import android.os.SystemClock

class TimeEngine(
    private var speedMultiplier: Float = 1.0f
) {
    private var startRealTime: Long = 0L
    private var pausedDisplayedTime: Long = 0L
    private var isRunning: Boolean = false

    fun updateSpeedMultiplier(multiplier: Float) {
        if (isRunning) {
            // Capture current displayed time before changing multiplier
            pausedDisplayedTime = getDisplayedElapsedTime()
            startRealTime = SystemClock.elapsedRealtime()
        }
        speedMultiplier = multiplier
    }

    fun start() {
        if (!isRunning) {
            startRealTime = SystemClock.elapsedRealtime()
            isRunning = true
        }
    }

    fun pause() {
        if (isRunning) {
            pausedDisplayedTime = getDisplayedElapsedTime()
            isRunning = false
        }
    }

    fun reset() {
        startRealTime = 0L
        pausedDisplayedTime = 0L
        isRunning = false
    }

    fun getDisplayedElapsedTime(): Long {
        return if (isRunning) {
            val realElapsed = SystemClock.elapsedRealtime() - startRealTime
            pausedDisplayedTime + (realElapsed * speedMultiplier).toLong()
        } else {
            pausedDisplayedTime
        }
    }

    fun isActive(): Boolean = isRunning

    companion object {
        fun calculateDisplayedTime(realElapsedMs: Long, speedMultiplier: Float): Long {
            return (realElapsedMs * speedMultiplier).toLong()
        }

        fun calculateRealTime(displayedMs: Long, speedMultiplier: Float): Long {
            return if (speedMultiplier > 0) {
                (displayedMs / speedMultiplier).toLong()
            } else {
                displayedMs
            }
        }
    }
}

class CountdownEngine(
    private var speedMultiplier: Float = 1.0f
) {
    private var totalDurationMs: Long = 0L
    private var startRealTime: Long = 0L
    private var displayedElapsedAtStart: Long = 0L
    private var isRunning: Boolean = false

    fun updateSpeedMultiplier(multiplier: Float) {
        if (isRunning) {
            displayedElapsedAtStart = getDisplayedElapsedTime()
            startRealTime = SystemClock.elapsedRealtime()
        }
        speedMultiplier = multiplier
    }

    fun setDuration(durationMs: Long) {
        totalDurationMs = durationMs
        reset()
    }

    fun start() {
        if (!isRunning && totalDurationMs > 0) {
            startRealTime = SystemClock.elapsedRealtime()
            isRunning = true
        }
    }

    fun pause() {
        if (isRunning) {
            displayedElapsedAtStart = getDisplayedElapsedTime()
            isRunning = false
        }
    }

    fun reset() {
        startRealTime = 0L
        displayedElapsedAtStart = 0L
        isRunning = false
    }

    private fun getDisplayedElapsedTime(): Long {
        return if (isRunning) {
            val realElapsed = SystemClock.elapsedRealtime() - startRealTime
            displayedElapsedAtStart + (realElapsed * speedMultiplier).toLong()
        } else {
            displayedElapsedAtStart
        }
    }

    fun getDisplayedRemainingTime(): Long {
        val elapsed = getDisplayedElapsedTime()
        return (totalDurationMs - elapsed).coerceAtLeast(0L)
    }

    fun isFinished(): Boolean {
        return getDisplayedRemainingTime() <= 0 && totalDurationMs > 0
    }

    fun isActive(): Boolean = isRunning

    fun getTotalDuration(): Long = totalDurationMs
}
