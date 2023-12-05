package com.example.semarv2.util.networkState

import com.example.semarv2.data.source.remote.model.User

data class UserState(
    val data: User? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
