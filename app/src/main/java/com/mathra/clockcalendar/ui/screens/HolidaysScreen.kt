package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.data.holidays.HolidayData
import com.mathra.clockcalendar.data.holidays.HolidayType
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.theme.*
import com.mathra.clockcalendar.util.DateUtils
import com.mathra.clockcalendar.util.appLocale

/** Gazetted years, plus the next year if it has no gazette yet (so people see a clear "not gazetted" note). */
private fun yearChoices(today: java.time.LocalDate): List<Int> =
    (2026..maxOf(HolidayData.LAST_GAZETTED_YEAR, today.year + 1)).toList()

/** Sri Lankan holidays + Poya days for a chosen year (2026 / 2027), each with a countdown. */
@Composable
fun HolidaysScreen() {
    val today = DateUtils.today()
    val availableYears = remember { yearChoices(today) }
    var year by remember { mutableStateOf(if (today.year in availableYears) today.year else availableYears.first()) }
    val holidays = remember(year) { HolidayData.forYear(year).sortedBy { it.date } }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MathraBackgroundGradient)
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.holidays_title, year), color = MathraGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        if (HolidayData.isGazetted(year)) {
            Text(
                stringResource(if (year == 2027) R.string.holidays_source_2027 else R.string.holidays_source_2026),
                color = MathraWhite.copy(alpha = 0.55f), fontSize = 11.sp
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            availableYears.forEach { y ->
                FilterChip(selected = year == y, onClick = { year = y }, label = { Text(y.toString()) })
            }
        }
        Spacer(Modifier.height(8.dp))

        if (!HolidayData.isGazetted(year)) {
            GlowCard {
                Column {
                    Text(stringResource(R.string.holidays_not_gazetted_title), color = MathraGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(stringResource(R.string.holidays_not_gazetted_body, year), color = MathraWhite, fontSize = 12.sp)
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(holidays) { h ->
                val resId = context.resources.getIdentifier(h.nameRes, "string", context.packageName)
                val label = if (resId != 0) stringResource(id = resId) else h.nameRes
                val daysAway = DateUtils.daysBetween(today, h.date).toInt()
                GlowCard {
                    Column {
                        Text(label, color = MathraWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(DateUtils.formatFullDate(h.date, appLocale()), color = MathraPurpleLight, fontSize = 12.sp)
                        Text(
                            when {
                                h.date == today -> stringResource(R.string.days_today)
                                h.date.isAfter(today) -> stringResource(R.string.days_in, daysAway)
                                else -> stringResource(R.string.days_ago, daysAway)
                            },
                            color = if (h.type == HolidayType.POYA_DAY) MathraBlue else MathraGold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
