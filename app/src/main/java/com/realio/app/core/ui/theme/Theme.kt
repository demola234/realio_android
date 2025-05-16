package com.realio.app.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColorDark,
    onPrimary = Color.Black,
    secondary = SecondaryColorDark,
    background = BackgroundColorDark,
    surface = BackgroundColorDark,
    onBackground = InvertedPrimaryColorDark,
    onSurface = InvertedSecondaryColorDark,
    tertiary = InvertedSecondaryColorLight,
    onError = RedOneColorDark,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColorLight,
    onPrimary = Color.White,
    secondary = SecondaryColorLight,
    background = BackgroundColorLight,
    surface = BackgroundColorLight,
    onBackground = InvertedPrimaryColorLight,
    onSurface = InvertedSecondaryColorLight,
    tertiary = NeutralTwoColorLight,
    onError = RedOneColorDark,
)

@Composable
fun RealioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}