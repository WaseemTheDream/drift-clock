package com.example.driftclock.ui.screens.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Input state as a string of digits (max 6 digits: HHMMSS)
    var inputDigits by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

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
                    onClick = {
                        viewModel.resetTimer()
                        inputDigits = ""
                    },
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
            // Timer input mode with numpad
            TimerInputDisplay(inputDigits = inputDigits)

            Spacer(modifier = Modifier.height(24.dp))

            // Number pad
            NumberPad(
                onDigitClick = { digit ->
                    if (inputDigits.length < 6) {
                        inputDigits += digit
                    }
                },
                onBackspace = {
                    if (inputDigits.isNotEmpty()) {
                        inputDigits = inputDigits.dropLast(1)
                    }
                },
                onClear = {
                    inputDigits = ""
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Start button
            val hasValidInput = inputDigits.isNotEmpty() && inputDigits.any { it != '0' }

            FilledIconButton(
                onClick = {
                    val (hours, minutes, seconds) = parseInputDigits(inputDigits)
                    viewModel.setInputHours(hours)
                    viewModel.setInputMinutes(minutes)
                    viewModel.setInputSeconds(seconds)
                    viewModel.startTimer()
                },
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
private fun TimerInputDisplay(inputDigits: String) {
    // Pad to 6 digits and split into HH MM SS
    val padded = inputDigits.padStart(6, '0')
    val hours = padded.substring(0, 2)
    val minutes = padded.substring(2, 4)
    val seconds = padded.substring(4, 6)

    // Determine which parts are "active" (user has typed into them)
    val totalDigits = inputDigits.length
    val hoursActive = totalDigits > 4
    val minutesActive = totalDigits > 2
    val secondsActive = totalDigits > 0

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Hours
        TimeUnitDisplay(
            value = hours,
            label = "h",
            isActive = hoursActive,
            isZero = hours == "00"
        )

        Spacer(modifier = Modifier.size(8.dp))

        // Minutes
        TimeUnitDisplay(
            value = minutes,
            label = "m",
            isActive = minutesActive,
            isZero = minutes == "00" && !hoursActive
        )

        Spacer(modifier = Modifier.size(8.dp))

        // Seconds
        TimeUnitDisplay(
            value = seconds,
            label = "s",
            isActive = secondsActive,
            isZero = seconds == "00" && !minutesActive
        )
    }
}

@Composable
private fun TimeUnitDisplay(
    value: String,
    label: String,
    isActive: Boolean,
    isZero: Boolean
) {
    val textColor = when {
        isActive && !isZero -> MaterialTheme.colorScheme.onSurface
        isActive -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    }

    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = value,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 56.sp,
                fontWeight = FontWeight.Light
            ),
            color = textColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun NumberPad(
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1: 1 2 3
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            NumPadButton("1") { onDigitClick("1") }
            NumPadButton("2") { onDigitClick("2") }
            NumPadButton("3") { onDigitClick("3") }
        }

        // Row 2: 4 5 6
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            NumPadButton("4") { onDigitClick("4") }
            NumPadButton("5") { onDigitClick("5") }
            NumPadButton("6") { onDigitClick("6") }
        }

        // Row 3: 7 8 9
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            NumPadButton("7") { onDigitClick("7") }
            NumPadButton("8") { onDigitClick("8") }
            NumPadButton("9") { onDigitClick("9") }
        }

        // Row 4: 00 0 backspace
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            NumPadButton("00") {
                onDigitClick("0")
                onDigitClick("0")
            }
            NumPadButton("0") { onDigitClick("0") }
            NumPadIconButton(
                icon = Icons.Default.Backspace,
                contentDescription = "Backspace",
                onClick = onBackspace
            )
        }
    }
}

@Composable
private fun NumPadButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.size(72.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun NumPadIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.size(72.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

private fun parseInputDigits(input: String): Triple<Int, Int, Int> {
    val padded = input.padStart(6, '0')
    val hours = padded.substring(0, 2).toIntOrNull() ?: 0
    val minutes = padded.substring(2, 4).toIntOrNull() ?: 0
    val seconds = padded.substring(4, 6).toIntOrNull() ?: 0
    return Triple(hours, minutes, seconds)
}
