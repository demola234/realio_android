package com.realio.app.feature.authentication.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.theme.RealioTheme
import com.realio.app.feature.authentication.data.model.onboardingDataList
import com.realio.app.feature.authentication.presentation.components.OnboardingPage
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController? = null,
) {
    val pageState = rememberPagerState(initialPage = 0) {
        onboardingDataList.size
    }
    val coroutineScope = rememberCoroutineScope()
    val buttonState = remember {
        derivedStateOf {
            when(pageState.currentPage) {
                0 -> listOf("", "Next")
                1 -> listOf("", "Next")
                2 -> listOf("Back", "Get Started")
                else -> listOf("", "")
            }
        }
    }

    Scaffold(
        bottomBar = {
            AppButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                content = {
                    Text(
                        text = buttonState.value[1],
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        )
                },
                shape = MaterialTheme.shapes,
                onClick = {
                    coroutineScope.launch {
                        if (pageState.currentPage >= 0 && pageState.currentPage < 2) {
                            pageState.animateScrollToPage(page = pageState.currentPage + 1)
                        } else {
                            // TODO: Navigate to the next screen
                            navController?.navigate(RealioScreenConsts.Login.name)
                        }
                    }
                }
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            HorizontalPager(state = pageState) { index ->
                OnboardingPage(
                    index = pageState.currentPage,
                    onboardingData =  onboardingDataList[index],
                )
            }

        }
    }
}

@Preview
@Composable
fun OnboardingPagePreview() {
    RealioTheme {
        OnboardingScreen()
    }
}