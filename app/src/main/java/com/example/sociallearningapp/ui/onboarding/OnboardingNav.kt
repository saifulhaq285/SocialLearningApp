package com.example.sociallearningapp.ui.onboarding

import android.content.Context
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sociallearningapp.util.AppPreferences
import kotlinx.coroutines.launch

object OnbRoutes {
    const val S1 = "onb1"
    const val S2 = "onb2"
    const val S3 = "onb3"
}

@Composable
fun OnboardingNav(
    context: Context,
    onFinished: () -> Unit
) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()
    // keep an InterstitialHolder so we can preload it
    val interstitial = remember { InterstitialHolder(context) }

    // preload interstitial once
    LaunchedEffect(Unit) { interstitial.load() }

    NavHost(navController = nav, startDestination = OnbRoutes.S1) {
        composable(OnbRoutes.S1) { OnboardingScreen1(onNext = { nav.navigate(OnbRoutes.S2) }) }
        composable(OnbRoutes.S2) {
            OnboardingScreen2(
                onNext = { nav.navigate(OnbRoutes.S3) },
                onBack = { nav.popBackStack() }
            )
        }
        composable(OnbRoutes.S3) {
            OnboardingScreen3(
                onBack = { nav.popBackStack() },
                onFinish = {
                    interstitial.showOr(
                        runIfUnavailable = {
                            // if no ad available, still mark onboarded and continue
                            scope.launch {
                                AppPreferences.setOnboarded(context, true)
                                onFinished()
                            }
                        },
                        onDismiss = {
                            // after ad dismissed, mark & continue
                            scope.launch {
                                AppPreferences.setOnboarded(context, true)
                                onFinished()
                            }
                        }
                    )
                }
            )
        }
    }
}