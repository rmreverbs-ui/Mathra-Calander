package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.theme.*
import com.mathra.clockcalendar.viewmodel.AlarmViewModel

@Composable
fun AlarmScreen() {
    val viewModel: AlarmViewModel = viewModel()
    val alarms by viewModel.alarms.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(MathraBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(stringResource(R.string.card_alarm), color = MathraGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            if (alarms.isEmpty()) {
                Text(stringResource(R.string.alarm_none_set), color = MathraWhite.copy(alpha = 0.6f), fontSize = 13.sp)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(alarms) { alarm ->
                        GlowCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        String.format("%02d:%02d", alarm.hour, alarm.minute),
                                        color = MathraWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold
                                    )
                                    Text(alarm.label, color = MathraPurpleLight, fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Switch(checked = alarm.isEnabled, onCheckedChange = { viewModel.toggleEnabled(alarm) })
                                    IconButton(onClick = { viewModel.delete(alarm) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = null, tint = MathraGold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = MathraGold,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.alarm_add), tint = MathraBgBlack)
        }
    }

    if (showDialog) {
        AddAlarmDialog(
            onDismiss = { showDialog = false },
            onConfirm = { hour, minute, label ->
                viewModel.addAlarm(hour, minute, label)
                showDialog = false
            }
        )
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun AddAlarmDialog(onDismiss: () -> Unit, onConfirm: (Int, Int, String) -> Unit) {
    val state = rememberTimePickerState(is24Hour = false)
    var label by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute, label.ifBlank { "Alarm" }) }) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text(stringResource(R.string.alarm_add)) },
        text = {
            Column {
                TimePicker(state = state)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text(stringResource(R.string.alarm_label)) })
            }
        }
    )
}
