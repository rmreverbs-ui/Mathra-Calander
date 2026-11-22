package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
fun StopwatchScreen() {
    var elapsedMillis by remember { mutableLongStateOf(0L) }
    var running by remember { mutableStateOf(false) }
    val laps = remember { mutableStateListOf<Long>() }

    LaunchedEffect(running) {
        var last = System.currentTimeMillis()
        while (running) {
            delay(37)
            val now = System.currentTimeMillis()
            elapsedMillis += now - last
            last = now
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MathraBackgroundGradient).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        GlowCard {
            Text(
                formatElapsed(elapsedMillis),
                color = MathraGold, fontSize = 44.sp, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { if (running) laps.add(elapsedMillis) else running = true }) {
                Text(if (running) stringResource(R.string.stopwatch_lap) else stringResource(R.string.stopwatch_start))
            }
            Button(onClick = { running = !running }) {
                Text(if (running) stringResource(R.string.stopwatch_pause) else stringResource(R.string.stopwatch_start))
            }
            Button(onClick = { running = false; elapsedMillis = 0; laps.clear() }) {
                Text(stringResource(R.string.stopwatch_reset))
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(laps.reversed()) { lap ->
                Text("Lap: ${formatElapsed(lap)}", color = MathraWhite, fontSize = 14.sp)
            }
        }
    }
}

private fun formatElapsed(millis: Long): String {
    val totalSeconds = millis / 1000
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    val cs = (millis % 1000) / 10
    return String.format("%02d:%02d.%02d", m, s, cs)
}
