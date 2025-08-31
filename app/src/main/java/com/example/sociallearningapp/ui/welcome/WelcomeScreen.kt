package com.example.sociallearningapp.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WelcomeScreen(
    onFinish: () -> Unit
) {
    val pages = listOf(
        "Welcome to Social Learning App",
        "Take quizzes and track your progress",
        "Manage tasks efficiently",
        "Chat with friends instantly"
    )
    var pageIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1e3c72), Color(0xFF2a5298))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Page Text
        Text(
            text = pages[pageIndex],
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        // Ad Banner
        AndroidView(
            factory = {
                MobileAds.initialize(context) {}
                AdView(context).apply {
                    // ✅ Use setAdSize instead of assigning to adSize
                    setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test ID
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )

        // Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (pageIndex > 0) pageIndex--
                },
                enabled = pageIndex > 0
            ) {
                Text("Previous")
            }

            Button(
                onClick = {
                    if (pageIndex < pages.size - 1) pageIndex++
                    else onFinish()
                }
            ) {
                Text(if (pageIndex == pages.size - 1) "Finish" else "Next")
            }
        }
    }
}