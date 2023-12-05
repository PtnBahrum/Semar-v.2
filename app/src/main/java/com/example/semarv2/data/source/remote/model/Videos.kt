package com.example.semarv2.data.source.remote.model

import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName


data class Videos(

    @DocumentId
    @field:SerializedName("id")
    val id : String = "",

    @field:SerializedName("title")
    val title : String = "",

    @field:SerializedName("image_thumbnail")
    val image_thumbnail: String="",

    @field:SerializedName("video_duration")
    val video_duration: String ="",

    @field:SerializedName("video_url")
    val video_url: String ="",

    @field:SerializedName("description")
    val description: String ="",

)
