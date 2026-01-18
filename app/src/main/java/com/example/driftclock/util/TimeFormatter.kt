package com.example.driftclock.util

object TimeFormatter {

    fun formatTimer(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun formatStopwatch(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val centiseconds = (milliseconds % 1000) / 10

        return if (hours > 0) {
            String.format("%d:%02d:%02d.%02d", hours, minutes, seconds, centiseconds)
        } else {
            String.format("%02d:%02d.%02d", minutes, seconds, centiseconds)
        }
    }

    fun formatLapTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val centiseconds = (milliseconds % 1000) / 10

        return String.format("%02d:%02d.%02d", minutes, seconds, centiseconds)
    }

    fun formatClockTime(hours: Int, minutes: Int): String {
        val amPm = if (hours < 12) "AM" else "PM"
        val displayHour = when {
            hours == 0 -> 12
            hours > 12 -> hours - 12
            else -> hours
        }
        return String.format("%d:%02d %s", displayHour, minutes, amPm)
    }

    fun parseInputToMillis(hours: Int, minutes: Int, seconds: Int): Long {
        return ((hours * 3600L) + (minutes * 60L) + seconds) * 1000L
    }
}
