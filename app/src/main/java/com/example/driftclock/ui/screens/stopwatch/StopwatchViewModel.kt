package com.example.driftclock.ui.screens.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driftclock.data.model.LapTime
import com.example.driftclock.data.model.StopwatchState
import com.example.driftclock.data.repository.PreferencesRepository
import com.example.driftclock.engine.TimeEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StopwatchViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val timeEngine = TimeEngine()
    private var updateJob: Job? = null

    private val _stopwatchState = MutableStateFlow(StopwatchState())
    val stopwatchState: StateFlow<StopwatchState> = _stopwatchState.asStateFlow()

    private var lastLapTime: Long = 0L

    init {
        viewModelScope.launch {
            preferencesRepository.currentSpeedMultiplier.collect { multiplier ->
                timeEngine.updateSpeedMultiplier(multiplier)
            }
        }
    }

    fun startStopwatch() {
        timeEngine.start()
        _stopwatchState.value = _stopwatchState.value.copy(isRunning = true)
        startUpdates()
    }

    fun pauseStopwatch() {
        timeEngine.pause()
        updateJob?.cancel()
        _stopwatchState.value = _stopwatchState.value.copy(isRunning = false)
    }

    fun resetStopwatch() {
        updateJob?.cancel()
        timeEngine.reset()
        lastLapTime = 0L
        _stopwatchState.value = StopwatchState()
    }

    fun recordLap() {
        if (!_stopwatchState.value.isRunning) return

        val currentTime = timeEngine.getDisplayedElapsedTime()
        val lapTime = currentTime - lastLapTime
        lastLapTime = currentTime

        val newLap = LapTime(
            lapNumber = _stopwatchState.value.laps.size + 1,
            lapTimeMs = lapTime,
            totalTimeMs = currentTime
        )

        _stopwatchState.value = _stopwatchState.value.copy(
            laps = listOf(newLap) + _stopwatchState.value.laps
        )
    }

    private fun startUpdates() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            while (_stopwatchState.value.isRunning) {
                _stopwatchState.value = _stopwatchState.value.copy(
                    displayedElapsedMs = timeEngine.getDisplayedElapsedTime()
                )
                delay(50) // ~20fps for smooth display
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
    }
}
