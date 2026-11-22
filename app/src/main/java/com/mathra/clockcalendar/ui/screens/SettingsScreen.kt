package com.mathra.clockcalendar.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.data.settings.AppLanguage
import com.mathra.clockcalendar.data.settings.ThemeMode
import com.mathra.clockcalendar.navigation.Screen
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.theme.*
import com.mathra.clockcalendar.util.canScheduleExact
import com.mathra.clockcalendar.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(navController: NavHostController) {
    val viewModel: SettingsViewModel = viewModel()
    val language by viewModel.language.collectAsState()
    val use24h by viewModel.use24HourFormat.collectAsState()
    val notifications by viewModel.notificationsEnabled.collectAsState()
    val vibration by viewModel.vibrationEnabled.collectAsState()
    val animations by viewModel.animationsEnabled.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val selectedTheme by viewModel.selectedTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MathraBackgroundGradient)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.nav_settings), color = MathraGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        ExactAlarmCard()

        GlowCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.settings_language), color = MathraWhite, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = language == AppLanguage.ENGLISH,
                        onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) },
                        label = { Text("English") }
                    )
                    Spacer(Modifier.width(8.dp))
                    FilterChip(
                        selected = language == AppLanguage.SINHALA,
                        onClick = { viewModel.setLanguage(AppLanguage.SINHALA) },
                        label = { Text("සිංහල") }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ---- Appearance: System / Dark / Light ----
        GlowCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.settings_appearance), color = MathraWhite, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemeMode.values().forEach { mode ->
                        FilterChip(
                            selected = themeMode == mode,
                            onClick = { viewModel.setThemeMode(mode) },
                            label = {
                                Text(
                                    stringResource(
                                        when (mode) {
                                            ThemeMode.SYSTEM -> R.string.mode_system
                                            ThemeMode.DARK -> R.string.mode_dark
                                            ThemeMode.LIGHT -> R.string.mode_light
                                        }
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ---- Theme Gallery ----
        GlowCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.settings_theme_gallery), color = MathraWhite, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MathraThemes.all.forEach { info ->
                        ThemeTile(
                            name = if (language == AppLanguage.SINHALA) info.nameSi else info.nameEn,
                            palette = MathraThemes.palette(info.id, ThemeState.isDark),
                            selected = selectedTheme == info.id,
                            onClick = { viewModel.setSelectedTheme(info.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        GlowCard { SettingSwitchRow(stringResource(R.string.clock_24h), use24h) { viewModel.setUse24HourFormat(it) } }
        Spacer(Modifier.height(12.dp))
        GlowCard { SettingSwitchRow(stringResource(R.string.settings_notifications), notifications) { viewModel.setNotificationsEnabled(it) } }
        Spacer(Modifier.height(12.dp))
        GlowCard { SettingSwitchRow(stringResource(R.string.settings_vibration), vibration) { viewModel.setVibrationEnabled(it) } }
        Spacer(Modifier.height(12.dp))
        GlowCard { SettingSwitchRow(stringResource(R.string.settings_animations), animations) { viewModel.setAnimationsEnabled(it) } }

        Spacer(Modifier.height(12.dp))
        GlowCard(onClick = { navController.navigate(Screen.About.route) }) {
            Text(stringResource(R.string.settings_about), color = MathraWhite, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ThemeTile(
    name: String,
    palette: MathraPalette,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(Brush.verticalGradient(listOf(palette.bg, palette.primaryDeep)))
            .border(
                BorderStroke(if (selected) 3.dp else 1.dp, if (selected) MathraGold else MathraWhite.copy(alpha = 0.25f)),
                shape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(palette.primary, palette.accent, palette.blue).forEach {
                Box(Modifier.size(14.dp).clip(CircleShape).background(it))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(name, color = palette.text, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 2)
    }
}

/** Shown only on Android 12 / 12L when exact-alarm access has not been granted. */
@Composable
private fun ExactAlarmCard() {
    val context = LocalContext.current
    var canExact by remember { mutableStateOf(context.canScheduleExact()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) canExact = context.canScheduleExact()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    if (canExact) return

    GlowCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_exact_alarm), color = MathraGold, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(stringResource(R.string.settings_exact_alarm_desc), color = MathraWhite, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${context.packageName}"))
                )
            }) { Text(stringResource(R.string.settings_exact_alarm_button)) }
        }
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun SettingSwitchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = MathraWhite)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
