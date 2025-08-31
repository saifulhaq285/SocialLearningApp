package com.example.sociallearningapp.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen1(onNext: () -> Unit) {
    ScreenScaffold(
        title = "Welcome to Social Learning",
        body = "Learn together — quizzes, tasks and group chat.",
        primary = "Next",
        onPrimary = onNext
    )
}

@Composable
fun OnboardingScreen2(onNext: () -> Unit, onBack: () -> Unit) {
    ScreenScaffold(
        title = "Key Features",
        body = "Timed quizzes • Personal tasks • Real-time chat",
        primary = "Next",
        onPrimary = onNext,
        secondary = "Back",
        onSecondary = onBack
    )
}

@Composable
fun OnboardingScreen3(onFinish: () -> Unit, onBack: () -> Unit) {
    ScreenScaffold(
        title = "Privacy & Terms",
        body = "We respect your privacy. By continuing, you accept the terms.",
        primary = "Finish",
        onPrimary = onFinish,
        secondary = "Back",
        onSecondary = onBack
    )
}

@Composable
private fun ScreenScaffold(
    title: String,
    body: String,
    primary: String,
    onPrimary: () -> Unit,
    secondary: String? = null,
    onSecondary: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(28.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        Text(body, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.weight(1f))

        // Banner ad (keeps it simple at bottom of screen)
        BannerAdView()
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            if (secondary != null && onSecondary != null) {
                OutlinedButton(onClick = onSecondary) { Text(secondary) }
            } else {
                Spacer(Modifier.width(8.dp))
            }
            Button(onClick = onPrimary) { Text(primary) }
        }
    }
}
