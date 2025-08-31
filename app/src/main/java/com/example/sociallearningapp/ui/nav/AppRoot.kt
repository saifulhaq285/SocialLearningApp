package com.example.sociallearningapp.ui.nav

import android.content.Context
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sociallearningapp.ui.auth.AuthNav
import com.example.sociallearningapp.ui.onboarding.OnboardingNav
import com.google.firebase.auth.FirebaseAuth
import com.example.sociallearningapp.util.AppPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object Root {
    const val AUTH = "auth"
    const val ONB = "onboarding"
    const val MAIN = "main"
}

@Composable
fun AppRoot(context: Context = LocalContext.current) {
    val nav = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()

    var startDest by remember { mutableStateOf<String?>(null) }

    // Decide start destination
    LaunchedEffect(Unit) {
        val onboarded = AppPreferences.onboardedFlow(context).first()
        startDest = when {
            auth.currentUser == null -> Root.AUTH
            !onboarded -> Root.ONB
            else -> Root.MAIN
        }
    }

    if (startDest == null) {
        Surface { Text("Loading...") }
    } else {
        NavHost(navController = nav, startDestination = startDest!!) {

            // 🔹 Auth Graph
            composable(Root.AUTH) {
                AuthNav(onAuthSuccess = {
                    nav.navigate(Root.MAIN) {
                        popUpTo(Root.AUTH) { inclusive = true }
                    }
                })
            }

            // 🔹 Onboarding Graph
            composable(Root.ONB) {
                OnboardingNav(context) {
                    scope.launch { AppPreferences.setOnboarded(context, true) }
                    nav.navigate(Root.MAIN) {
                        popUpTo(Root.ONB) { inclusive = true }
                    }
                }
            }

            // 🔹 Main Graph
            composable(Root.MAIN) {
                MainNav(
                    onLogout = {
                        // ✅ After logout → clear everything and go back to AUTH
                        nav.navigate(Root.AUTH) {
                            popUpTo(0) { inclusive = true } // 🔥 FIX
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}