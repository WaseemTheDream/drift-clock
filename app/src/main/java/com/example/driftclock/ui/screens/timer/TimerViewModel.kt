package com.example.driftclock.ui.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driftclock.data.model.TimerState
import com.example.driftclock.data.repository.PreferencesRepository
import com.example.driftclock.engine.CountdownEngine
import com.example.driftclock.util.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TimerViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val countdownEngine = CountdownEngine()
    private var timerJob: Job? = null

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _inputHours = MutableStateFlow(0)
    val inputHours: StateFlow<Int> = _inputHours.asStateFlow()

    private val _inputMinutes = MutableStateFlow(0)
    val inputMinutes: StateFlow<Int> = _inputMinutes.asStateFlow()

    private val _inputSeconds = MutableStateFlow(0)
    val inputSeconds: StateFlow<Int> = _inputSeconds.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.currentSpeedMultiplier.collect { multiplier ->
                countdownEngine.updateSpeedMultiplier(multiplier)
            }
        }
    }

    fun setInputHours(hours: Int) {
        _inputHours.value = hours.coerceIn(0, 99)
    }

    fun setInputMinutes(minutes: Int) {
        _inputMinutes.value = minutes.coerceIn(0, 59)
    }

    fun setInputSeconds(seconds: Int) {
        _inputSeconds.value = seconds.coerceIn(0, 59)
    }

    fun startTimer() {
        val totalMs = ((_inputHours.value * 3600L) + (_inputMinutes.value * 60L) + _inputSeconds.value) * 1000L

        if (totalMs <= 0) return

        viewModelScope.launch {
            val multiplier = preferencesRepository.currentSpeedMultiplier.first()
            countdownEngine.updateSpeedMultiplier(multiplier)
        }

        countdownEngine.setDuration(totalMs)
        countdownEngine.start()

        _timerState.value = TimerState(
            initialDurationMs = totalMs,
            displayedRemainingMs = totalMs,
            isRunning = true
        )

        startTimerUpdates()
    }

    fun resumeTimer() {
        countdownEngine.start()
        _timerState.value = _timerState.value.copy(
            isRunning = true,
            isPaused = false
        )
        startTimerUpdates()
    }

    fun pauseTimer() {
        countdownEngine.pause()
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(
            isRunning = false,
            isPaused = true
        )
    }

    fun resetTimer() {
        timerJob?.cancel()
        countdownEngine.reset()
        _timerState.value = TimerState()
        notificationHelper.cancelTimerNotification()
    }

    private fun startTimerUpdates() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.isRunning && !countdownEngine.isFinished()) {
                val remaining = countdownEngine.getDisplayedRemainingTime()
                _timerState.value = _timerState.value.copy(
                    displayedRemainingMs = remaining
                )
                delay(50) // Update at ~20fps for smooth display
            }

            if (countdownEngine.isFinished()) {
                _timerState.value = _timerState.value.copy(
                    displayedRemainingMs = 0,
                    isRunning = false,
                    isFinished = true
                )
                notificationHelper.showTimerCompleteNotification()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
