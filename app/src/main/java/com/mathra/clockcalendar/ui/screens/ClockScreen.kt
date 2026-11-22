package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.theme.*
import com.mathra.clockcalendar.util.appLocale
import com.mathra.clockcalendar.util.DateUtils
import com.mathra.clockcalendar.viewmodel.ClockViewModel
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockScreen() {
    val viewModel: ClockViewModel = viewModel()
    val time by viewModel.time.collectAsState()
    val date by viewModel.date.collectAsState()
    var use24h by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MathraBackgroundGradient)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))
        AnalogClockFace(time = time, modifier = Modifier.size(240.dp))
        Spacer(Modifier.height(24.dp))

        GlowCard {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (use24h) DateUtils.formatTime24(time) else DateUtils.formatTime12(time, appLocale()),
                    color = MathraGold,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(DateUtils.formatFullDate(date, appLocale()), color = MathraWhite, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.clock_24h), color = MathraWhite, fontSize = 13.sp)
                    Spacer(Modifier.width(8.dp))
                    Switch(checked = use24h, onCheckedChange = { use24h = it })
                }
            }
        }
    }
}

@Composable
private fun AnalogClockFace(time: LocalTime, modifier: Modifier = Modifier) {
    val secondFraction = time.second + time.nano / 1_000_000_000f
    val minuteFraction = time.minute + secondFraction / 60f
    val hourFraction = (time.hour % 12) + minuteFraction / 60f

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        drawCircle(color = MathraPurpleDeep, radius = radius, center = center)
        drawCircle(color = MathraGold, radius = radius, center = center, style = Stroke(width = 6f))

        // Hour ticks
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30 - 90).toDouble())
            val outer = Offset(center.x + radius * 0.92f * cos(angle).toFloat(), center.y + radius * 0.92f * sin(angle).toFloat())
            val inner = Offset(center.x + radius * 0.80f * cos(angle).toFloat(), center.y + radius * 0.80f * sin(angle).toFloat())
            drawLine(MathraWhite, inner, outer, strokeWidth = 4f, cap = StrokeCap.Round)
        }

        fun hand(fraction: Float, lengthRatio: Float, width: Float, color: androidx.compose.ui.graphics.Color, degreesPerUnit: Float) {
            val angle = Math.toRadians((fraction * degreesPerUnit - 90).toDouble())
            val end = Offset(center.x + radius * lengthRatio * cos(angle).toFloat(), center.y + radius * lengthRatio * sin(angle).toFloat())
            drawLine(color, center, end, strokeWidth = width, cap = StrokeCap.Round)
        }

        hand(hourFraction, 0.5f, 10f, MathraWhite, 30f)
        hand(minuteFraction, 0.75f, 6f, MathraBlue, 6f)
        hand(secondFraction, 0.85f, 3f, MathraGold, 6f)

        drawCircle(color = MathraGold, radius = 10f, center = center)
    }
}
