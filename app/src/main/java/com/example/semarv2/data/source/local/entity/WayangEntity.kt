package com.example.semarv2.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName= "wayangs")
data class WayangEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "name")
    val name : String = "",

    @ColumnInfo(name = "characteristic")
    val characteristic : String = "",

    @ColumnInfo(name = "parent")
    val parent : String = "",

    @ColumnInfo(name = "sibling")
    val sibling : String = "",

    @ColumnInfo(name = "image")
    val image : List<String> = emptyList(),

    @ColumnInfo(name = "image_thumbnail")
    val image_thumbnail : String = "",

    @ColumnInfo(name = "description")
    val description : String = "",
)