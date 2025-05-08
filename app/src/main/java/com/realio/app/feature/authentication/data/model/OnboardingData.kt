package com.realio.app.feature.authentication.data.model

import androidx.annotation.DrawableRes
import com.realio.app.R

data class OnboardingData(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val onboardingDataList = listOf(
    OnboardingData(
        title = "Discover beautiful \n" +
                "Apartments all over \n" +
                "the world!",
        description = "Welcome to the world of beautiful apartments! We are excited to have you join our real estate community, and we can't wait to show you all the amazing apartments waiting for you to discover.",
        image = R.drawable.onboarding1
    ),
    OnboardingData(
        title = "Explore your dream\n" +
                "Apartment!",
        description = "As you explore our app, you'll find that we've made it incredibly easy to find your dream apartment. Our user-friendly interface allows you to filter apartments by location, price, size, and amenities to ensure you find exactly what you're looking for.",
        image = R.drawable.onboarding2
    ),
    OnboardingData(
        title = "Become an Agent and sell your Properties!",
        description = "We know that searching for an apartment can be overwhelming, but we're here to make the process as smooth and stress-free as possible. Our team of experts is always on hand to answer any questions you might have and offer personalized advice to ensure you find the perfect place to call home.",
        image = R.drawable.onboarding3
    ),
)