package com.example.driftclock.ui.screens.stopwatch

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Time display
        Text(
            text = TimeFormatter.formatStopwatch(state.displayedElapsedMs),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.isActive) {
                // Reset button (shows when active)
                FilledIconButton(
                    onClick = { viewModel.resetStopwatch() },
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
                modifier = Modifier.size(80.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isRunning) "Pause" else "Start",
                    modifier = Modifier.size(40.dp)
                )
            }

            // Lap button (shows when running)
            if (state.isRunning) {
                FilledIconButton(
                    onClick = { viewModel.recordLap() },
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Lap",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Lap list
        if (state.laps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(state.laps) { lap ->
                    LapItem(lap = lap)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun LapItem(lap: LapTime) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Lap ${lap.lapNumber}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = TimeFormatter.formatLapTime(lap.lapTimeMs),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = TimeFormatter.formatLapTime(lap.totalTimeMs),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
