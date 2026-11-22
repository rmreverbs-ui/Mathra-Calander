package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun TimerScreen() {
    var inputMinutes by remember { mutableStateOf("5") }
    var totalSeconds by remember { mutableIntStateOf(5 * 60) }
    var remainingSeconds by remember { mutableIntStateOf(totalSeconds) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
        if (remainingSeconds <= 0) running = false
    }

    val progress = if (totalSeconds > 0) remainingSeconds / totalSeconds.toFloat() else 0f

    Column(
        modifier = Modifier.fillMaxSize().background(MathraBackgroundGradient).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        GlowCard {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material3.LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = MathraGold, trackColor = MathraPurpleDeep
                )
                Spacer(Modifier.height(12.dp))
                Text(formatSeconds(remainingSeconds), color = MathraGold, fontSize = 44.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Spacer(Modifier.height(20.dp))

        if (!running) {
            OutlinedTextField(
                value = inputMinutes,
                onValueChange = { inputMinutes = it.filter(Char::isDigit) },
                label = { Text("Minutes") }
            )
            Spacer(Modifier.height(12.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                if (!running) {
                    val mins = inputMinutes.toIntOrNull() ?: 5
                    totalSeconds = mins * 60
                    remainingSeconds = totalSeconds
                }
                running = !running
            }) {
                Text(if (running) stringResource(R.string.timer_pause) else stringResource(R.string.timer_start))
            }
            Button(onClick = { running = false; remainingSeconds = totalSeconds }) {
                Text(stringResource(R.string.timer_reset))
            }
        }
    }
}

private fun formatSeconds(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return String.format("%02d:%02d", m, s)
}
