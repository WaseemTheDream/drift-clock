package com.example.driftclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.driftclock.data.datastore.UserPreferencesDataStore
import com.example.driftclock.data.repository.PreferencesRepository
import com.example.driftclock.ui.navigation.DriftClockBottomNavBar
import com.example.driftclock.ui.navigation.DriftClockNavGraph
import com.example.driftclock.ui.theme.DriftClockTheme
import com.example.driftclock.util.NotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataStore = UserPreferencesDataStore(applicationContext)
        val preferencesRepository = PreferencesRepository(dataStore)
        val notificationHelper = NotificationHelper(applicationContext)

        setContent {
            val navController = rememberNavController()
            val preferences by preferencesRepository.userPreferences.collectAsState(
                initial = com.example.driftclock.data.model.UserPreferences()
            )

            DriftClockTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        DriftClockBottomNavBar(navController = navController)
                    }
                ) { innerPadding ->
                    DriftClockNavGraph(
                        navController = navController,
                        preferencesRepository = preferencesRepository,
                        notificationHelper = notificationHelper,
                        hasCompletedOnboarding = preferences.hasCompletedOnboarding,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
