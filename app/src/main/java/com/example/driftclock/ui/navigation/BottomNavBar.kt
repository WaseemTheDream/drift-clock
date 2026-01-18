package com.example.driftclock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class NavBarItemData(
    val item: BottomNavItem,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val navBarItems = listOf(
    NavBarItemData(BottomNavItem.ALARM, Icons.Filled.Alarm, Icons.Outlined.Alarm),
    NavBarItemData(BottomNavItem.CLOCK, Icons.Filled.Schedule, Icons.Outlined.Schedule),
    NavBarItemData(BottomNavItem.TIMER, Icons.Filled.Timer, Icons.Outlined.Timer),
    NavBarItemData(BottomNavItem.STOPWATCH, Icons.Filled.Timer, Icons.Outlined.Timer),
    NavBarItemData(BottomNavItem.BEDTIME, Icons.Filled.Bedtime, Icons.Outlined.Bedtime)
)

@Composable
fun DriftClockBottomNavBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Don't show bottom bar on profile selection or hidden settings
    if (currentRoute == Routes.ProfileSelection.route || currentRoute == Routes.HiddenSettings.route) {
        return
    }

    NavigationBar {
        navBarItems.forEach { navItem ->
            val isSelected = currentRoute == navItem.item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) navItem.selectedIcon else navItem.unselectedIcon,
                        contentDescription = navItem.item.label
                    )
                },
                label = { Text(navItem.item.label) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != navItem.item.route) {
                        navController.navigate(navItem.item.route) {
                            popUpTo(Routes.Timer.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
