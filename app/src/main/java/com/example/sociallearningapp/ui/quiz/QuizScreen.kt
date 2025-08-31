package com.example.sociallearningapp.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuizScreen(
    key: Int, // ✅ new param for restart
    vm: QuizViewModel = viewModel(key = key.toString()), // ✅ new instance each restart
    onFinished: (Int) -> Unit
) {
    val index by vm.index.collectAsState()
    val score by vm.score.collectAsState()
    val timeLeft by vm.timeLeft.collectAsState()

    LaunchedEffect(index) {
        if (index >= 0) vm.startTimer()
        else onFinished(score)
    }

    if (index >= 0) {
        val q = vm.currentQuestion
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1e3c72), // deep blue
                            Color(0xFF2a5298)  // lighter blue
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ Timer with dynamic color
            val timerColor = if (timeLeft in 1..3) Color.Red else Color.White
            Text("Time left: $timeLeft", style = MaterialTheme.typography.bodyLarge, color = timerColor)

            Spacer(Modifier.height(16.dp))
            Text(q.text, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Spacer(Modifier.height(16.dp))
            q.options.forEachIndexed { i, opt ->
                Button(
                    onClick = { vm.answer(i) },
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                ) { Text(opt) }
            }
        }
    }
}