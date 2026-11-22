package com.mathra.clockcalendar.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.navigation.Screen
import com.mathra.clockcalendar.ui.theme.MathraBgDeep
import com.mathra.clockcalendar.ui.theme.MathraGold
import com.mathra.clockcalendar.ui.theme.MathraWhite

private data class NavItem(val screen: Screen, val labelRes: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val items = listOf(
    NavItem(Screen.Dashboard, R.string.nav_home, Icons.Filled.Home),
    NavItem(Screen.Calendar, R.string.nav_calendar, Icons.Filled.DateRange),
    NavItem(Screen.Clock, R.string.nav_clock, Icons.Filled.Schedule),
    NavItem(Screen.Reminders, R.string.nav_reminders, Icons.Filled.Notifications),
    NavItem(Screen.Settings, R.string.nav_settings, Icons.Filled.Settings),
)

@Composable
fun MathraBottomNavBar(currentRoute: String?, navController: NavHostController) {
    NavigationBar(containerColor = MathraBgDeep) {
        items.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = null, tint = if (selected) MathraGold else MathraWhite) },
                label = { Text(stringResourceCompat(item.labelRes), color = if (selected) MathraGold else MathraWhite) }
            )
        }
    }
}

// Small helper so this file has exactly one clear place using stringResource with a Composable context.
@Composable
private fun stringResourceCompat(resId: Int): String = androidx.compose.ui.res.stringResource(id = resId)
