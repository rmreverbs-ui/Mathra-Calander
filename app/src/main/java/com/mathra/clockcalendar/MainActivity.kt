package com.mathra.clockcalendar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mathra.clockcalendar.navigation.MathraNavGraph
import com.mathra.clockcalendar.viewmodel.SettingsViewModel
import com.mathra.clockcalendar.ui.theme.MathraTheme

/**
 * Single-activity host for the whole app. All screens are Compose
 * destinations driven by [MathraNavGraph].
 */
class MainActivity : AppCompatActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op: UI reflects granted state via settings screen */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissionIfNeeded()

        setContent {
            val settings: SettingsViewModel = viewModel()
            val themeId by settings.selectedTheme.collectAsState()
            val themeMode by settings.themeMode.collectAsState()
            MathraTheme(themeId = themeId, themeMode = themeMode) {
                MathraNavGraph()
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
