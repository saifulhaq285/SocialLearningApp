package com.example.sociallearningapp.ui.nav
// ui/nav/NavRoutes.kt
sealed class Dest(val route: String, val label: String) {
    object Quiz : Dest("quiz", "Quiz")
    object Tasks : Dest("tasks", "Tasks")
    object Chat : Dest("chat", "Chat")
    object Profile : Dest("profile", "Profile")
}
val bottomDestinations = listOf(Dest.Quiz, Dest.Tasks, Dest.Chat, Dest.Profile)
