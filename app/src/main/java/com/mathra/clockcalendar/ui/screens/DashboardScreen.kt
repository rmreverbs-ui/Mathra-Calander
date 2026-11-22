package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.data.holidays.HolidayData
import com.mathra.clockcalendar.navigation.Screen
import com.mathra.clockcalendar.data.holidays.HolidayType
import com.mathra.clockcalendar.ui.components.FestiveDecor
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.components.PandaMascot
import com.mathra.clockcalendar.ui.theme.*
import com.mathra.clockcalendar.util.appLocale
import com.mathra.clockcalendar.util.DateUtils
import com.mathra.clockcalendar.viewmodel.ClockViewModel
import java.time.LocalTime

private data class DashCard(val titleRes: Int, val icon: ImageVector, val route: String)

private val dashCards = listOf(
    DashCard(R.string.card_clock, Icons.Filled.Schedule, Screen.Clock.route),
    DashCard(R.string.card_calendar, Icons.Filled.DateRange, Screen.Calendar.route),
    DashCard(R.string.card_alarm, Icons.Filled.Alarm, Screen.Alarm.route),
    DashCard(R.string.card_stopwatch, Icons.Filled.Timer, Screen.Stopwatch.route),
    DashCard(R.string.card_timer, Icons.Filled.HourglassBottom, Screen.Timer.route),
    DashCard(R.string.card_reminders, Icons.Filled.Notifications, Screen.Reminders.route),
    DashCard(R.string.card_holidays, Icons.Filled.Celebration, Screen.Holidays.route),
    DashCard(R.string.card_settings, Icons.Filled.Settings, Screen.Settings.route),
    DashCard(R.string.card_about, Icons.Filled.Info, Screen.About.route),
)

@Composable
fun DashboardScreen(navController: NavHostController) {
    val clockViewModel: ClockViewModel = viewModel()
    val time by clockViewModel.time.collectAsState()
    val date by clockViewModel.date.collectAsState()
    val upcoming = remember { HolidayData.nextUpcoming(DateUtils.today(), 3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MathraBackgroundGradient)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            // Dress the panda when a festival is within a week.
            val soon = upcoming.firstOrNull()?.takeIf { DateUtils.daysBetween(DateUtils.today(), it.date) <= 7 }
            PandaMascot(
                size = 88,
                festivalKey = soon?.festivalKey ?: if (soon?.type == HolidayType.POYA_DAY) "poya" else null
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(greetingFor(time), color = MathraGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(DateUtils.formatFullDate(date, appLocale()), color = MathraWhite, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        GlowCard {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = DateUtils.formatTime12(time, appLocale()),
                    color = MathraWhite,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(stringResource(R.string.upcoming_holidays), color = MathraGold, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                upcoming.forEach { h ->
                    val resId = androidx.compose.ui.platform.LocalContext.current
                        .resources.getIdentifier(h.nameRes, "string", androidx.compose.ui.platform.LocalContext.current.packageName)
                    val label = if (resId != 0) stringResource(id = resId) else h.nameRes
                    Text("• $label — ${h.date}", color = MathraWhite, fontSize = 12.sp)
                }
            }
            // Vector festival motif for the next upcoming festival / Poya day.
            val next = upcoming.firstOrNull()
            FestiveDecor(
                key = next?.festivalKey ?: if (next?.type == HolidayType.POYA_DAY) "poya" else null
            )
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(dashCards) { card ->
                GlowCard(onClick = { navController.navigate(card.route) }, modifier = Modifier.height(100.dp)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Icon(card.icon, contentDescription = null, tint = MathraGold)
                        Spacer(Modifier.height(6.dp))
                        Text(stringResource(id = card.titleRes), color = MathraWhite, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
private fun greetingFor(time: LocalTime): String {
    val h = time.hour
    return stringResource(
        id = when {
            h < 12 -> R.string.greeting_morning
            h < 17 -> R.string.greeting_afternoon
            h < 21 -> R.string.greeting_evening
            else -> R.string.greeting_night
        }
    )
}
