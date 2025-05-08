package com.realio.app.feature.authentication.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import com.realio.app.feature.authentication.data.model.OnboardingData
import com.realio.app.feature.authentication.data.model.onboardingDataList


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    index: Int,
    onboardingData: OnboardingData
) {

    Column(

    ) {
        Image(
            painter = painterResource(id = onboardingData.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(24.dp)
                .height(330.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PADDING_LARGE))
        PagerIndicatorComponent(
            modifier = Modifier.padding(vertical = PADDING_LARGE, horizontal = PADDING_LARGE),
            pagesSize = onboardingDataList.size,
            selectedPage = index
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

@Preview
@Composable
fun OnboardingPagePreview() {
    OnboardingPage(
        index = 0,
        onboardingData = onboardingDataList[0]
    )
}