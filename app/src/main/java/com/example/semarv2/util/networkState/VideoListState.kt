package com.example.semarv2.util.networkState

import com.example.semarv2.data.source.local.entity.VideoEntity

data class VideoListState(
    val data: List<VideoEntity>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)