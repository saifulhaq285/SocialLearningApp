package com.example.sociallearningapp.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
}

@Composable
fun AuthNav(onAuthSuccess: () -> Unit) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = AuthRoutes.LOGIN) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = onAuthSuccess,
                onRegisterClick = { nav.navigate(AuthRoutes.REGISTER) }
            )
        }
        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = onAuthSuccess,
                onLoginClick = { nav.popBackStack() }
            )
        }
    }
}
