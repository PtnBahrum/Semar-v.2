package com.example.semarv2.data.source.remote.model

import androidx.room.Entity


data class Materi(
    val title : String,
    val image : String,
    val materi_html : String,
    val materi_url : String,
    val description : String
)
