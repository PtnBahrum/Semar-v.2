package com.example.semarv2.util.networkState

import com.example.semarv2.data.source.remote.model.User
import com.example.semarv2.data.source.local.entity.VideoEntity
import com.example.semarv2.data.source.local.entity.WayangEntity
import kotlinx.coroutines.flow.Flow

data class VideoState(
    val data: Flow<VideoEntity?>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)