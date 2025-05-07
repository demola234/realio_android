package com.realio.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.realio.app.feature.authentication.data.model.onboardingData
import com.realio.app.feature.authentication.presentation.screen.OnboardingScreen

@Composable
fun RealioAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RealioScreenConsts.Onboarding.name
    ) {
        // Onboarding screen route
        composable(route = RealioScreenConsts.Onboarding.name) {
            OnboardingScreen(onboardingData = onboardingData[0])
        }
    }
}