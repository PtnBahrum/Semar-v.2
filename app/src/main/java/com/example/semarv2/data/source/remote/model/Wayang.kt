package com.example.semarv2.data.source.remote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName


data class Wayang(

    @field:SerializedName("id")
    val id : String ="",

    @field:SerializedName("name")
    val name : String = "",

    @field:SerializedName("characteristic")
    val characteristic: String = "",

    @field:SerializedName("description")
    val description: String ="",

    @field:SerializedName("image")
    val image: List<String> = emptyList(),

    @field:SerializedName("image_thumbnail")
    val image_thumbnail: String="",

    @field:SerializedName("parent")
    val parent: String ="",

    @field:SerializedName("sibling")
    val sibling: String =""
)
