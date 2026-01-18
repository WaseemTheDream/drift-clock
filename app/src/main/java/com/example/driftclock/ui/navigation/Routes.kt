package com.example.driftclock.ui.navigation

sealed class Routes(val route: String) {
    object ProfileSelection : Routes("profile_selection")
    object Alarm : Routes("alarm")
    object Clock : Routes("clock")
    object Timer : Routes("timer")
    object Stopwatch : Routes("stopwatch")
    object Bedtime : Routes("bedtime")
    object HiddenSettings : Routes("hidden_settings")
}

enum class BottomNavItem(
    val route: String,
    val label: String
) {
    ALARM(Routes.Alarm.route, "Alarm"),
    CLOCK(Routes.Clock.route, "Clock"),
    TIMER(Routes.Timer.route, "Timer"),
    STOPWATCH(Routes.Stopwatch.route, "Stopwatch"),
    BEDTIME(Routes.Bedtime.route, "Bedtime")
}
