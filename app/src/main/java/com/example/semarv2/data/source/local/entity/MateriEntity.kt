package com.example.semarv2.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "materi")
data class MateriEntity(

    @DocumentId
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id :String = "",

    @ColumnInfo(name = "title")
    val title : String = "",

    @ColumnInfo(name = "image")
    val image : String ="",

    @ColumnInfo(name = "materi_html")
    val materi_html : String ="",

    @ColumnInfo(name = "materi_url")
    val materi_url : String ="",

    @ColumnInfo(name = "description")
    val description : String = ""
)