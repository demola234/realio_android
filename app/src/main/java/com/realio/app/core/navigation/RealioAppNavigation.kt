package com.realio.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.realio.app.core.common.EnhancedAvatarSelectionScreen
import com.realio.app.feature.authentication.presentation.screen.AvatarSelectionScreen
import com.realio.app.feature.authentication.presentation.screen.LoginScreen
import com.realio.app.feature.authentication.presentation.screen.OnboardingScreen
import com.realio.app.feature.authentication.presentation.screen.OtpVerificationScreen
import com.realio.app.feature.authentication.presentation.screen.PersonalizationExperienceScreen
import com.realio.app.feature.authentication.presentation.screen.SignUpScreen
import com.realio.app.feature.authentication.presentation.viewModel.GoogleViewModel
import com.realio.app.feature.authentication.presentation.viewModel.LoginViewModel

@Composable
fun RealioAppNavigation() {
    val navController = rememberNavController()
    val authViewModel: GoogleViewModel = viewModel()
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
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                loginViewModel = loginViewModel,
                onSignInSuccess = { userData ->
                    // Navigate to the home screen after successful sign-in
                    navController.navigate(RealioScreenConsts.PersonalInfo.name) {
                        popUpTo(RealioScreenConsts.PersonalInfo.name) { inclusive = true }
                    }
                },
                webClientId = "445004992065-47q6ru2g0b0mks6j8pq22798aqlgdqqi.apps.googleusercontent.com",

                )
        }
        // Register screen route
        composable(route = RealioScreenConsts.SignUp.name) {
            SignUpScreen(navController = navController)
        }
        // Otp screen route
        composable(
            route = "${RealioScreenConsts.Otp.name}/{emailQuery}",
            arguments = listOf(
                navArgument(name = "emailQuery") {
                    type = NavType.StringType
                }
            )
        ) { navBack ->
            navBack.arguments?.getString("emailQuery")?.let { email ->
                OtpVerificationScreen(
                    navController = navController,
                    email = email
                )
            }
        }

        // Personalized screen route
        composable(route = RealioScreenConsts.PersonalInfo.name) {
            PersonalizationExperienceScreen(navController = navController)
        }
        // Avatar screen route
        composable(route = RealioScreenConsts.Avatar.name) {
            AvatarSelectionScreen(navController = navController)
        }
    }
}