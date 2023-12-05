package com.example.semarv2.features.onboarding.customView

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.semarv2.R

enum class OnBoardingPage(
    @StringRes val titleResource: Int,
    @StringRes val descriptionResource: Int,
    @DrawableRes val logoResource: Int
) {

    ONE(R.string.onboarding_slide1_title,R.string.onboarding_slide1_desc, R.drawable.onboarding_image_1),
    TWO(R.string.onboarding_slide2_title,R.string.onboarding_slide2_desc, R.drawable.onboarding_image_2),
    THREE(R.string.onboarding_slide3_title,R.string.onboarding_slide1_desc, R.drawable.onboarding_image_3)

}