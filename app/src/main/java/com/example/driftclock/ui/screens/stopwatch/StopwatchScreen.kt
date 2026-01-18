package com.example.driftclock.ui.screens.stopwatch

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.driftclock.data.model.LapTime
import com.example.driftclock.data.repository.PreferencesRepository
import com.example.driftclock.util.TimeFormatter

@Composable
fun StopwatchScreen(
    preferencesRepository: PreferencesRepository
) {
    val viewModel = remember {
        StopwatchViewModel(preferencesRepository)
    }

    val state by viewModel.stopwatchState.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        // Calculate adaptive sizes
        val hasLaps = state.laps.isNotEmpty()

        // Button sizes scale with screen width
        val primaryButtonSize = (maxWidth * 0.22f).coerceIn(64.dp, 88.dp)
        val secondaryButtonSize = (maxWidth * 0.17f).coerceIn(52.dp, 72.dp)

        // Font size scales with width
        val timeFontSize = when {
            maxWidth < 300.dp -> 48.sp
            maxWidth < 360.dp -> 56.sp
            maxWidth < 420.dp -> 64.sp
            else -> 72.sp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Time display section
            Box(
                modifier = Modifier
                    .weight(if (hasLaps) 0.30f else 0.45f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = TimeFormatter.formatStopwatch(state.displayedElapsedMs),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = timeFontSize,
                        fontWeight = FontWeight.Light
                    )
                )
            }

            // Control buttons section
            Box(
                modifier = Modifier
                    .weight(if (hasLaps) 0.20f else 0.35f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reset button (shows when active)
                    if (state.isActive) {
                        FilledIconButton(
                            onClick = { viewModel.resetStopwatch() },
                            modifier = Modifier.size(secondaryButtonSize),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Reset",
                                modifier = Modifier.size(secondaryButtonSize * 0.45f)
                            )
                        }
                    }

                    // Play/Pause button
                    FilledIconButton(
                        onClick = {
                            if (state.isRunning) {
                                viewModel.pauseStopwatch()
                            } else {
                                viewModel.startStopwatch()
                            }
                        },
                        modifier = Modifier.size(primaryButtonSize),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (state.isRunning) "Pause" else "Start",
                            modifier = Modifier.size(primaryButtonSize * 0.45f)
                        )
                    }

                    // Lap button (shows when running)
                    if (state.isRunning) {
                        FilledIconButton(
                            onClick = { viewModel.recordLap() },
                            modifier = Modifier.size(secondaryButtonSize),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = "Lap",
                                modifier = Modifier.size(secondaryButtonSize * 0.45f)
                            )
                        }
                    }
                }
            }

            // Lap list section
            if (hasLaps) {
                Box(
                    modifier = Modifier
                        .weight(0.50f)
                        .fillMaxWidth()
                ) {
                    Column {
                        HorizontalDivider()

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            items(state.laps) { lap ->
                                LapItem(lap = lap)
                                HorizontalDivider()
                            }
                        }
                    }
                }
            } else {
                // Empty space when no laps
                Spacer(modifier = Modifier.weight(0.20f))
            }
        }
    }
}

@Composable
private fun LapItem(lap: LapTime) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Lap ${lap.lapNumber}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = TimeFormatter.formatLapTime(lap.lapTimeMs),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = TimeFormatter.formatLapTime(lap.totalTimeMs),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
