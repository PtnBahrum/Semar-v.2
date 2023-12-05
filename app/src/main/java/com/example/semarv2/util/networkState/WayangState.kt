package com.example.semarv2.util.networkState

import com.example.semarv2.data.source.local.entity.WayangEntity
import kotlinx.coroutines.flow.Flow

data class WayangState(
    val data: WayangEntity? = null,
    val error: String = "",
    val isLoading: Boolean = false
)