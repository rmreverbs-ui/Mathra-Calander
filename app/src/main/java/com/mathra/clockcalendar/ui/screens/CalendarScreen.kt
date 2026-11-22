package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.data.holidays.HolidayType
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.theme.*
import com.mathra.clockcalendar.util.appLocale
import com.mathra.clockcalendar.util.DateUtils
import com.mathra.clockcalendar.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(navController: NavHostController) {
    val viewModel: CalendarViewModel = viewModel()
    val visibleMonth by viewModel.visibleMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val grid = remember(visibleMonth) { viewModel.gridFor(visibleMonth) }
    val today = DateUtils.today()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MathraBackgroundGradient)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.previousMonth() }) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = null, tint = MathraGold)
            }
            Text(
                "${visibleMonth.month.getDisplayName(TextStyle.FULL, appLocale())} ${visibleMonth.year}",
                color = MathraWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MathraGold)
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DateUtils.weekdayLabels(appLocale()).forEach { label ->
                Text(label, color = MathraPurpleLight, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.weight(1f)) {
            items(grid) { date ->
                val inMonth = date.monthValue == visibleMonth.monthValue
                val isToday = date == today
                val isSelected = date == selectedDate
                val holidays = viewModel.holidaysFor(date)

                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> MathraGold
                                isToday -> MathraPurple
                                else -> androidx.compose.ui.graphics.Color.Transparent
                            }
                        )
                        .clickable { viewModel.selectDate(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (!inMonth) MathraWhite.copy(alpha = 0.3f) else if (isSelected) MathraBgBlack else MathraWhite,
                            fontSize = 13.sp,
                            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (holidays.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (holidays.any { it.type == HolidayType.POYA_DAY }) MathraBlue else MathraGold
                                    )
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        val selectedHolidays = viewModel.holidaysFor(selectedDate)
        GlowCard {
            Column {
                Text(DateUtils.formatFullDate(selectedDate, appLocale()), color = MathraGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (selectedHolidays.isEmpty()) {
                    Text("—", color = MathraWhite.copy(alpha = 0.6f), fontSize = 13.sp)
                } else {
                    selectedHolidays.forEach { h ->
                        val resId = context.resources.getIdentifier(h.nameRes, "string", context.packageName)
                        val label = if (resId != 0) stringResource(id = resId) else h.nameRes
                        val typeLabel = when (h.type) {
                            HolidayType.POYA_DAY -> stringResource(R.string.calendar_poya_day)
                            HolidayType.PUBLIC_HOLIDAY -> stringResource(R.string.calendar_public_holiday)
                            else -> ""
                        }
                        Text("• $label  ($typeLabel)", color = MathraWhite, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
