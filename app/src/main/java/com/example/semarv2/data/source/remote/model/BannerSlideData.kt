package com.example.semarv2.data.source.remote.model

import com.example.semarv2.R

data class BannerSlideData(
    val id : Int,
    val type : String,
    val img : Int,
)
object dataSlide{
    val data = listOf<BannerSlideData>(
        BannerSlideData(1,"kuis", R.drawable.bg_kuis_banner),
        BannerSlideData(2,"wayang",R.drawable.bg_wayang_banner),
        BannerSlideData(3,"video", R.drawable.bg_video_banner)
    )
}
