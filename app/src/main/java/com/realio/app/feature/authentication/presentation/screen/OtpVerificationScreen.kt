package com.realio.app.feature.authentication.presentation.screen

import OtpInputField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OtpVerificationScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.Gray
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var otpValue by remember { mutableStateOf("") }
            var isError by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }
            var successMessage by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Verification Code",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Enter the 6-digit code sent to your phone",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )


                    OtpInputField(
                        otpLength = 6,
                        isError = isError,
                        errorMessage = errorMessage,
                        enableSmsAutofill = true,
                        onOtpComplete = { code ->
                            // Handle OTP completion
                            println("OTP entered: $code")
                            if (code.length == 6 && code.all { it.isDigit() } && code == "123456") {
                                // OTP is valid, proceed with verification
                                successMessage = "OTP verification successful!"
                                isError = false
                                // You can navigate to the next screen or perform other actions
                            } else {
                                isError = true
                                errorMessage = "Please enter a valid OTP"
                            }
                        },
                        modifier = Modifier.padding(vertical = 16.dp)
                    )


                if (successMessage.isNotEmpty()) {
                    Text(
                        text = successMessage,
                        color = Color(0xFF4CAF50), // Green
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // TODO: Trigger resend logic here
                        isError = false
                        errorMessage = ""
                        successMessage = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Resend Code")
                }
            }
        }
    }
}
