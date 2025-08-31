package com.example.sociallearningapp.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun ScoreScreen(score: Int, totalQuestions: Int = 5, onRestart: () -> Unit) {
    val emoji = when {
        score == totalQuestions -> "🎉"
        score >= totalQuestions / 2 -> "👍"
        else -> "😅"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF673AB7), Color(0xFF2196F3)) // Purple → Blue
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section: Score + Emoji + Restart button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "You got $score / $totalQuestions correct",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Text(
                emoji,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRestart) {
                Text("Restart Quiz")
            }
        }

        // Bottom section: Banner Ad
        AndroidView(factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // ✅ Test Banner
                loadAd(AdRequest.Builder().build())
            }
        })
    }
}