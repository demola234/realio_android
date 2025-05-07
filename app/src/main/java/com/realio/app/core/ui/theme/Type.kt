package com.realio.app.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.realio.app.R

val UrbanistFontFamily = FontFamily(
    Font(R.font.urbanist_thin, FontWeight.Thin),
    Font(R.font.urbanist_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.urbanist_extra_light, FontWeight.ExtraLight),
    Font(R.font.urbanist_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.urbanist_light, FontWeight.Light),
    Font(R.font.urbanist_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.urbanist_medium, FontWeight.Medium),
    Font(R.font.urbanist_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.urbanist_semi_bold, FontWeight.SemiBold),
    Font(R.font.urbanist_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.urbanist_bold, FontWeight.Bold),
    Font(R.font.urbanist_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.urbanist_extra_bold, FontWeight.ExtraBold),
    Font(R.font.urbanist_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.urbanist_black, FontWeight.Black),
    Font(R.font.urbanist_black_italic, FontWeight.Black, FontStyle.Italic),
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = -0.25.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelSmall = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
