package com.example.semarv2.util.networkState

import com.example.semarv2.data.source.remote.model.Wayang
import com.example.semarv2.data.source.local.entity.WayangEntity
import com.example.semarv2.util.Resource
import kotlinx.coroutines.flow.Flow

data class WayangListState(
    val data: List<WayangEntity>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
