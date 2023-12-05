package com.example.semarv2.util.networkState

import com.example.semarv2.data.source.remote.model.User

data class ForgotPasswordState(
    val data: Unit? = null,
    val error: String = "",
    val isLoading: Boolean = false
)