package com.mathra.clockcalendar.navigation

/** All navigable destinations in the app. */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Dashboard : Screen("dashboard")
    data object Clock : Screen("clock")
    data object Calendar : Screen("calendar")
    data object Holidays : Screen("holidays")
    data object Alarm : Screen("alarm")
    data object Stopwatch : Screen("stopwatch")
    data object Timer : Screen("timer")
    data object Reminders : Screen("reminders")
    data object Settings : Screen("settings")
    data object About : Screen("about")
}
