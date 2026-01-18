package com.example.driftclock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.driftclock.data.repository.PreferencesRepository
import com.example.driftclock.ui.screens.alarm.AlarmScreen
import com.example.driftclock.ui.screens.bedtime.BedtimeScreen
import com.example.driftclock.ui.screens.clock.ClockScreen
import com.example.driftclock.ui.screens.profile.ProfileSelectionScreen
import com.example.driftclock.ui.screens.settings.HiddenSettingsScreen
import com.example.driftclock.ui.screens.stopwatch.StopwatchScreen
import com.example.driftclock.ui.screens.timer.TimerScreen
import com.example.driftclock.util.NotificationHelper

@Composable
fun DriftClockNavGraph(
    navController: NavHostController,
    preferencesRepository: PreferencesRepository,
    notificationHelper: NotificationHelper,
    hasCompletedOnboarding: Boolean,
    modifier: Modifier = Modifier
) {
    val startDestination = if (hasCompletedOnboarding) {
        Routes.Timer.route
    } else {
        Routes.ProfileSelection.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.ProfileSelection.route) {
            ProfileSelectionScreen(
                preferencesRepository = preferencesRepository,
                onProfileSelected = {
                    navController.navigate(Routes.Timer.route) {
                        popUpTo(Routes.ProfileSelection.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Alarm.route) {
            AlarmScreen()
        }

        composable(Routes.Clock.route) {
            ClockScreen()
        }

        composable(Routes.Timer.route) {
            TimerScreen(
                preferencesRepository = preferencesRepository,
                notificationHelper = notificationHelper
            )
        }

        composable(Routes.Stopwatch.route) {
            StopwatchScreen(
                preferencesRepository = preferencesRepository
            )
        }

        composable(Routes.Bedtime.route) {
            BedtimeScreen(
                onHiddenSettingsAccess = {
                    navController.navigate(Routes.HiddenSettings.route)
                }
            )
        }

        composable(Routes.HiddenSettings.route) {
            HiddenSettingsScreen(
                preferencesRepository = preferencesRepository,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
