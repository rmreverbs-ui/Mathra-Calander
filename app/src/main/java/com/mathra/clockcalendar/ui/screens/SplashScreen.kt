package com.mathra.clockcalendar.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.navigation.Screen
import com.mathra.clockcalendar.ui.components.PandaMascot
import com.mathra.clockcalendar.ui.theme.*
import kotlinx.coroutines.delay

/** Animated splash: scales/fades the panda logo in, then auto-navigates to the dashboard. */
@Composable
fun SplashScreen(navController: NavHostController) {
    var started by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (started) 1f else 0.6f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "splash_scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(900),
        label = "splash_alpha"
    )

    LaunchedEffect(Unit) {
        started = true
        delay(2200)
        navController.navigate(Screen.Dashboard.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MathraBackgroundGradient),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PandaMascot(modifier = Modifier.scale(scale).alpha(alpha), size = 180)
        Spacer(Modifier.height(24.dp))
        Text(
            text = "MATHRA",
            color = MathraGold,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.scale(scale)
        )
        Text(
            text = "CLOCK AND CALANDER",
            color = MathraWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.tagline), color = MathraPurpleLight, fontSize = 13.sp)
    }
}
