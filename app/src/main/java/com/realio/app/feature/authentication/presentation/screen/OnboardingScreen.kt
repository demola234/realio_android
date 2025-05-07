package com.realio.app.feature.authentication.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.realio.app.R
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import com.realio.app.core.common.Dimensions.PADDING_SMALL
import com.realio.app.feature.authentication.data.model.OnboardingData
import com.realio.app.feature.authentication.presentation.components.PagerIndicatorComponent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onboardingData: OnboardingData
) {
    Scaffold { paddingValues ->
        Column(

            modifier = modifier.padding(paddingValues)
        ) {
            Image(
                modifier = Modifier.padding(PADDING_LARGE),
                alignment = Alignment.Center,
                painter = painterResource(id = onboardingData.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                )
            Spacer(modifier = Modifier.height(PADDING_LARGE))
            PagerIndicatorComponent(
                modifier = Modifier.padding(vertical = PADDING_LARGE),
                pagesSize = 3,
                selectedPage = 0,
                selectedColor = MaterialTheme.colorScheme.onBackground,
                unselectedColor = MaterialTheme.colorScheme.inverseOnSurface
            )
            Spacer(modifier = Modifier.height(PADDING_LARGE))
            Text(
                onboardingData.title,
                modifier = Modifier.padding(horizontal = PADDING_LARGE),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
                )
            Spacer(modifier = Modifier.height(PADDING_LARGE))
            Text(
                onboardingData.description,
                modifier = Modifier.padding(horizontal = PADDING_LARGE),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

