package com.example.semarv2.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class PredictWayang(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("result")
    val result: String,

    @field:SerializedName("score")
    val score : Float,
)