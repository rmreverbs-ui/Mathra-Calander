package com.mathra.clockcalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathra.clockcalendar.R
import com.mathra.clockcalendar.ui.components.GlowCard
import com.mathra.clockcalendar.ui.components.PandaMascot
import com.mathra.clockcalendar.ui.theme.*

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(MathraBackgroundGradient).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        PandaMascot(size = 120)
        Spacer(Modifier.height(12.dp))
        Text("MATHRA CLOCK AND CALANDER", color = MathraGold, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text(stringResource(R.string.tagline), color = MathraPurpleLight, fontSize = 13.sp)
        Spacer(Modifier.height(16.dp))

        GlowCard {
            Column {
                Text(stringResource(R.string.about_version), color = MathraWhite)
                Spacer(Modifier.height(4.dp))
                Text(stringResource(R.string.about_developer), color = MathraWhite)
                Spacer(Modifier.height(8.dp))
                Text(
                    "A premium clock, calendar, Sri Lankan holiday and festival companion with the Mathra panda as your time-keeping guide.",
                    color = MathraWhite.copy(alpha = 0.85f), fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Text(stringResource(R.string.about_copyright), color = MathraWhite.copy(alpha = 0.5f), fontSize = 11.sp)
        Spacer(Modifier.height(12.dp))
    }
}
