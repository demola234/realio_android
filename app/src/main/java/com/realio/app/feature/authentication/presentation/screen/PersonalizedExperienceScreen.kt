package com.realio.app.feature.authentication.presentation.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.theme.RealioTheme

@Composable
fun PersonalizationExperienceScreen(navController: NavController? = null) {
    val propertyTypes = listOf("Bungalo", "Studio", "Multi-bedroom", "Loft", "Duplex", "Penthouse", "Garden", "Luxury")

    val selectedProperties = remember {
        mutableStateListOf<String>().apply {
            // Pre-selected options
            add("Bungalo")
            add("Multi-bedroom")
            add("Garden")
            add("Luxury")
        }
    }
    Scaffold (
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable {
                                navController?.popBackStack()
                            }
                    )
                }

                // Title
                Text(
                    text = "Personalise your experience",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subtitle
                Text(
                    text = "What Type of Properties are looking for?",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Property options
                propertyTypes.forEach { property ->
                    val isSelected = property in selectedProperties

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Color.DarkGray.copy(alpha = 0.5f)
                                else Color.Transparent
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Transparent else Color.DarkGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                if (isSelected) {
                                    selectedProperties.remove(property)
                                } else {
                                    selectedProperties.add(property)
                                }
                            }
                    ) {
                        Text(
                            text = property,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp)
                        )

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Next button
                Button(
                    onClick = { navController?.navigate(RealioScreenConsts.Avatar.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7986CB)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Next",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalizationExperienceScreenPreview() {
    RealioTheme {
        PersonalizationExperienceScreen(
            navController = null,
        )
    }
}
