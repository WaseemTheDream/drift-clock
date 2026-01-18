package com.example.driftclock.ui.screens.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.driftclock.data.repository.PreferencesRepository
import com.example.driftclock.util.NotificationHelper
import com.example.driftclock.util.TimeFormatter

@Composable
fun TimerScreen(
    preferencesRepository: PreferencesRepository,
    notificationHelper: NotificationHelper
) {
    val viewModel = remember {
        TimerViewModel(preferencesRepository, notificationHelper)
    }

    val timerState by viewModel.timerState.collectAsState()
    val inputHours by viewModel.inputHours.collectAsState()
    val inputMinutes by viewModel.inputMinutes.collectAsState()
    val inputSeconds by viewModel.inputSeconds.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        if (timerState.isActive || timerState.isFinished) {
            // Timer running/paused/finished display
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(280.dp)
            ) {
                CircularProgressIndicator(
                    progress = { timerState.progress },
                    modifier = Modifier.size(280.dp),
                    strokeWidth = 8.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = when {
                        timerState.isFinished -> MaterialTheme.colorScheme.tertiary
                        timerState.isPaused -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (timerState.isFinished) "00:00" else TimeFormatter.formatTimer(timerState.displayedRemainingMs),
                        style = MaterialTheme.typography.displayLarge
                    )
                    if (timerState.isFinished) {
                        Text(
                            text = "Time's up!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Control buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset button
                FilledIconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Reset",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Play/Pause button
                if (!timerState.isFinished) {
                    FilledIconButton(
                        onClick = {
                            if (timerState.isRunning) {
                                viewModel.pauseTimer()
                            } else {
                                viewModel.resumeTimer()
                            }
                        },
                        modifier = Modifier.size(80.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (timerState.isRunning) "Pause" else "Resume",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        } else {
            // Timer input
            Text(
                text = "Set timer",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeInputField(
                    value = inputHours,
                    onValueChange = { viewModel.setInputHours(it) },
                    label = "Hours"
                )

                Text(
                    text = ":",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                TimeInputField(
                    value = inputMinutes,
                    onValueChange = { viewModel.setInputMinutes(it) },
                    label = "Minutes"
                )

                Text(
                    text = ":",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                TimeInputField(
                    value = inputSeconds,
                    onValueChange = { viewModel.setInputSeconds(it) },
                    label = "Seconds"
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Quick presets
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickPresetButton(text = "1:00") {
                    viewModel.setInputHours(0)
                    viewModel.setInputMinutes(1)
                    viewModel.setInputSeconds(0)
                }
                QuickPresetButton(text = "5:00") {
                    viewModel.setInputHours(0)
                    viewModel.setInputMinutes(5)
                    viewModel.setInputSeconds(0)
                }
                QuickPresetButton(text = "10:00") {
                    viewModel.setInputHours(0)
                    viewModel.setInputMinutes(10)
                    viewModel.setInputSeconds(0)
                }
                QuickPresetButton(text = "30:00") {
                    viewModel.setInputHours(0)
                    viewModel.setInputMinutes(30)
                    viewModel.setInputSeconds(0)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Start button
            val hasValidInput = inputHours > 0 || inputMinutes > 0 || inputSeconds > 0

            FilledIconButton(
                onClick = { viewModel.startTimer() },
                modifier = Modifier.size(80.dp),
                enabled = hasValidInput,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
private fun TimeInputField(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = if (value == 0) "" else value.toString(),
            onValueChange = { text ->
                val newValue = text.filter { it.isDigit() }.take(2).toIntOrNull() ?: 0
                onValueChange(newValue)
            },
            modifier = Modifier.width(72.dp),
            textStyle = MaterialTheme.typography.headlineLarge.copy(
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickPresetButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(text = text)
    }
}
