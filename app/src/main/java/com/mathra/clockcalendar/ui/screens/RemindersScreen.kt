package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.data.local.ReminderEntity
import com.mathra.clockcalendar.data.local.ReminderRepeat
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.theme.*
import com.mathra.clockcalendar.util.appLocale
import com.mathra.clockcalendar.util.DateUtils
import com.mathra.clockcalendar.viewmodel.RemindersViewModel
import com.mathra.clockcalendar.viewmodel.SettingsViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RemindersScreen() {
    val viewModel: RemindersViewModel = viewModel()
    val reminders by viewModel.reminders.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(MathraBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(stringResource(R.string.card_reminders), color = MathraGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            if (reminders.isEmpty()) {
                Text(stringResource(R.string.reminder_none), color = MathraWhite.copy(alpha = 0.6f), fontSize = 13.sp)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(reminders, key = { it.id }) { reminder ->
                        ReminderRow(
                            reminder = reminder,
                            onToggle = { viewModel.toggleCompleted(reminder) },
                            onDelete = { viewModel.delete(reminder) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = MathraGold,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.reminder_add), tint = MathraBgBlack)
        }
    }

    if (showDialog) {
        AddReminderDialog(
            onDismiss = { showDialog = false },
            onSave = { title, note, millis, repeat ->
                viewModel.addReminder(title, millis, note, repeat)
                showDialog = false
            }
        )
    }
}

@Composable
private fun ReminderRow(reminder: ReminderEntity, onToggle: () -> Unit, onDelete: () -> Unit) {
    GlowCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    reminder.title, color = MathraWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                    textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else null
                )
                val dateTime = Instant.ofEpochMilli(reminder.epochMillis).atZone(DateUtils.SRI_LANKA_ZONE)
                Text(
                    dateTime.format(DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a", appLocale())),
                    color = MathraPurpleLight, fontSize = 12.sp
                )
                if (reminder.repeat != ReminderRepeat.NONE) {
                    Text(repeatLabel(reminder.repeat), color = MathraGold, fontSize = 11.sp)
                }
                if (reminder.note.isNotBlank()) {
                    Text(reminder.note, color = MathraWhite.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = reminder.isCompleted, onCheckedChange = { onToggle() })
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = null, tint = MathraGold)
                }
            }
        }
    }
}

@Composable
private fun repeatLabel(repeat: ReminderRepeat): String = stringResource(
    when (repeat) {
        ReminderRepeat.NONE -> R.string.repeat_none
        ReminderRepeat.DAILY -> R.string.repeat_daily
        ReminderRepeat.YEARLY_BIRTHDAY -> R.string.repeat_birthday
        ReminderRepeat.YEARLY_ANNIVERSARY -> R.string.repeat_anniversary
    }
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun AddReminderDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, note: String, epochMillis: Long, repeat: ReminderRepeat) -> Unit
) {
    val settings: SettingsViewModel = viewModel()
    val use24h by settings.use24HourFormat.collectAsState()

    val zone = DateUtils.SRI_LANKA_ZONE
    val defaultStart = remember { LocalDateTime.now(zone).plusHours(1).withSecond(0).withNano(0) }

    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(defaultStart.toLocalDate()) }
    var time by remember { mutableStateOf(defaultStart.toLocalTime()) }
    var repeat by remember { mutableStateOf(ReminderRepeat.NONE) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val epochMillis = LocalDateTime.of(date, time).atZone(zone).toInstant().toEpochMilli()
    val timeFormatter = if (use24h) DateTimeFormatter.ofPattern("HH:mm") else DateTimeFormatter.ofPattern("hh:mm a", appLocale())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.reminder_add)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it; showError = false },
                    label = { Text(stringResource(R.string.reminder_title_hint)) },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = note, onValueChange = { note = it },
                    label = { Text(stringResource(R.string.reminder_note_hint)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.weight(1f)) {
                        Text(date.format(DateTimeFormatter.ofPattern("d MMM yyyy", appLocale())))
                    }
                    OutlinedButton(onClick = { showTimePicker = true }, modifier = Modifier.weight(1f)) {
                        Text(time.format(timeFormatter))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.reminder_repeat), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ReminderRepeat.values().forEach { option ->
                        FilterChip(
                            selected = repeat == option,
                            onClick = { repeat = option; showError = false },
                            label = { Text(repeatLabel(option), fontSize = 12.sp) }
                        )
                    }
                }
                if (showError) {
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.reminder_past_error), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when {
                    title.isBlank() -> Unit // a title is required, so an empty save does nothing
                    repeat == ReminderRepeat.NONE && epochMillis <= System.currentTimeMillis() -> showError = true
                    else -> onSave(title.trim(), note.trim(), epochMillis, repeat)
                }
            }) { Text(stringResource(R.string.action_save)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) } }
    )

    if (showDatePicker) {
        val todayUtc = LocalDate.now(zone).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        // Repeating reminders (e.g. birthdays) may start from a past date; one-off ones must be today or later.
        val selectable = remember(repeat) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) =
                    repeat != ReminderRepeat.NONE || utcTimeMillis >= todayUtc
            }
        }
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
            selectableDates = selectable
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let {
                        date = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                        showError = false
                    }
                    showDatePicker = false
                }) { Text(stringResource(R.string.action_ok)) }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.action_cancel)) } }
        ) { DatePicker(state = pickerState) }
    }

    if (showTimePicker) {
        val timeState = rememberTimePickerState(
            initialHour = time.hour, initialMinute = time.minute, is24Hour = use24h
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    time = LocalTime.of(timeState.hour, timeState.minute)
                    showError = false
                    showTimePicker = false
                }) { Text(stringResource(R.string.action_ok)) }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text(stringResource(R.string.action_cancel)) } },
            text = { TimePicker(state = timeState) }
        )
    }
}
