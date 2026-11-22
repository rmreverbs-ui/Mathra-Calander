package com.mathra.clockcalendar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import com.mathra.clockcalendar.ui.components.MathraBottomNavBar
import com.mathra.clockcalendar.ui.screens.*

/**
 * Root navigation graph. The splash screen has no bottom bar; every other
 * screen is wrapped in a Scaffold with the shared [MathraBottomNavBar].
 */
@Composable
fun MathraNavGraph(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute != Screen.Splash.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MathraBottomNavBar(currentRoute = currentRoute, navController = navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Screen.Splash.route) { SplashScreen(navController) }
            composable(Screen.Dashboard.route) { DashboardScreen(navController) }
            composable(Screen.Clock.route) { ClockScreen() }
            composable(Screen.Calendar.route) { CalendarScreen(navController) }
            composable(Screen.Holidays.route) { HolidaysScreen() }
            composable(Screen.Alarm.route) { AlarmScreen() }
            composable(Screen.Stopwatch.route) { StopwatchScreen() }
            composable(Screen.Timer.route) { TimerScreen() }
            composable(Screen.Reminders.route) { RemindersScreen() }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.About.route) { AboutScreen() }
        }
    }
}
