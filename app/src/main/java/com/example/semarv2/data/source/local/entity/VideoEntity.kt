package com.example.semarv2.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName

@Entity(tableName = "videos")
data class VideoEntity(
    @DocumentId
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id : String= "",

    @ColumnInfo(name = "title")
    val title : String = "",

    @ColumnInfo(name = "image_thumbnail")
    val image_thumbnail: String="",

    @ColumnInfo(name = "video_duration")
    val video_duration: String ="",

    @ColumnInfo(name = "video_url")
    val video_url: String ="",

    @ColumnInfo(name = "description")
    val description: String ="",
)