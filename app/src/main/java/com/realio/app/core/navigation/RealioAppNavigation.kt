package com.realio.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.realio.app.feature.authentication.presentation.screen.LoginScreen
import com.realio.app.feature.authentication.presentation.screen.OnboardingScreen
import com.realio.app.feature.authentication.presentation.screen.SignUpScreen

@Composable
fun RealioAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RealioScreenConsts.Onboarding.name
    ) {
        // Onboarding screen route
        composable(route = RealioScreenConsts.Onboarding.name) {
            OnboardingScreen(navController = navController)
        }
        // Login screen route
        composable(route = RealioScreenConsts.Login.name) {
            LoginScreen(navController = navController)
        }
        // Register screen route
        composable(route = RealioScreenConsts.SignUp.name) {
            SignUpScreen(navController = navController)
        }
    }
}