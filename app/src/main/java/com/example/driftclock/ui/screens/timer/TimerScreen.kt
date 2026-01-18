package com.example.driftclock.ui.screens.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
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
    var inputDigits by remember { mutableStateOf("") }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        if (timerState.isActive || timerState.isFinished) {
            // Timer running/paused/finished display
            TimerRunningContent(
                timerState = timerState,
                maxWidth = maxWidth,
                onReset = {
                    viewModel.resetTimer()
                    inputDigits = ""
                },
                onPauseResume = {
                    if (timerState.isRunning) {
                        viewModel.pauseTimer()
                    } else {
                        viewModel.resumeTimer()
                    }
                }
            )
        } else {
            // Timer input mode with numpad
            TimerInputContent(
                inputDigits = inputDigits,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
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
                onStart = {
                    val (hours, minutes, seconds) = parseInputDigits(inputDigits)
                    viewModel.setInputHours(hours)
                    viewModel.setInputMinutes(minutes)
                    viewModel.setInputSeconds(seconds)
                    viewModel.startTimer()
                }
            )
        }
    }
}

@Composable
private fun TimerRunningContent(
    timerState: com.example.driftclock.data.model.TimerState,
    maxWidth: Dp,
    onReset: () -> Unit,
    onPauseResume: () -> Unit
) {
    val progressSize = min(maxWidth - 32.dp, 300.dp)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(progressSize)
        ) {
            CircularProgressIndicator(
                progress = { timerState.progress },
                modifier = Modifier.size(progressSize),
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = onReset,
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

            if (!timerState.isFinished) {
                FilledIconButton(
                    onClick = onPauseResume,
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
    }
}

@Composable
private fun TimerInputContent(
    inputDigits: String,
    maxWidth: Dp,
    maxHeight: Dp,
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onStart: () -> Unit
) {
    // Calculate adaptive sizes
    val horizontalPadding = 24.dp
    val availableWidth = maxWidth - horizontalPadding

    // Numpad: 3 buttons per row with spacing
    val numpadSpacing = 12.dp
    val buttonSize = ((availableWidth - (numpadSpacing * 2)) / 3).coerceIn(60.dp, 90.dp)

    // Calculate based on available height
    val availableHeight = maxHeight - 32.dp // padding
    val displayHeight = availableHeight * 0.18f
    val numpadHeight = (buttonSize + numpadSpacing) * 4
    val startButtonSize = 72.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Time display - takes proportional space at top
        Box(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TimerInputDisplay(inputDigits = inputDigits, maxWidth = maxWidth)
        }

        // Number pad - takes most of the space
        Box(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AdaptiveNumberPad(
                buttonSize = buttonSize,
                spacing = numpadSpacing,
                onDigitClick = onDigitClick,
                onBackspace = onBackspace
            )
        }

        // Start button - fixed at bottom
        Box(
            modifier = Modifier
                .weight(0.20f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val hasValidInput = inputDigits.isNotEmpty() && inputDigits.any { it != '0' }

            FilledIconButton(
                onClick = onStart,
                modifier = Modifier.size(startButtonSize),
                enabled = hasValidInput,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
private fun TimerInputDisplay(inputDigits: String, maxWidth: Dp) {
    val padded = inputDigits.padStart(6, '0')
    val hours = padded.substring(0, 2)
    val minutes = padded.substring(2, 4)
    val seconds = padded.substring(4, 6)

    val totalDigits = inputDigits.length
    val hoursActive = totalDigits > 4
    val minutesActive = totalDigits > 2
    val secondsActive = totalDigits > 0

    // Scale font size based on width
    val fontSize = when {
        maxWidth < 300.dp -> 42.sp
        maxWidth < 400.dp -> 52.sp
        else -> 60.sp
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        TimeUnitDisplay(value = hours, label = "h", isActive = hoursActive, fontSize = fontSize)
        Spacer(modifier = Modifier.size(4.dp))
        TimeUnitDisplay(value = minutes, label = "m", isActive = minutesActive, fontSize = fontSize)
        Spacer(modifier = Modifier.size(4.dp))
        TimeUnitDisplay(value = seconds, label = "s", isActive = secondsActive, fontSize = fontSize)
    }
}

@Composable
private fun TimeUnitDisplay(
    value: String,
    label: String,
    isActive: Boolean,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    val textColor = if (isActive) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
    }

    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = value,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = fontSize,
                fontWeight = FontWeight.Light
            ),
            color = textColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = (fontSize.value / 6).dp)
        )
    }
}

@Composable
private fun AdaptiveNumberPad(
    buttonSize: Dp,
    spacing: Dp,
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            NumPadButton("1", buttonSize) { onDigitClick("1") }
            NumPadButton("2", buttonSize) { onDigitClick("2") }
            NumPadButton("3", buttonSize) { onDigitClick("3") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            NumPadButton("4", buttonSize) { onDigitClick("4") }
            NumPadButton("5", buttonSize) { onDigitClick("5") }
            NumPadButton("6", buttonSize) { onDigitClick("6") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            NumPadButton("7", buttonSize) { onDigitClick("7") }
            NumPadButton("8", buttonSize) { onDigitClick("8") }
            NumPadButton("9", buttonSize) { onDigitClick("9") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            NumPadButton("00", buttonSize) {
                onDigitClick("0")
                onDigitClick("0")
            }
            NumPadButton("0", buttonSize) { onDigitClick("0") }
            NumPadIconButton(
                icon = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Backspace",
                buttonSize = buttonSize,
                onClick = onBackspace
            )
        }
    }
}

@Composable
private fun NumPadButton(
    text: String,
    buttonSize: Dp,
    onClick: () -> Unit
) {
    val fontSize = when {
        buttonSize < 70.dp -> 20.sp
        buttonSize < 80.dp -> 24.sp
        else -> 28.sp
    }

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.size(buttonSize)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSize),
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun NumPadIconButton(
    icon: ImageVector,
    contentDescription: String,
    buttonSize: Dp,
    onClick: () -> Unit
) {
    val iconSize = buttonSize * 0.35f

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.size(buttonSize)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize)
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
