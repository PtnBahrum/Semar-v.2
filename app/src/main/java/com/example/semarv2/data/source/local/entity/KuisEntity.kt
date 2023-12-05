package com.example.semarv2.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "kuis")
data class KuisEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "image")
    val image: String = "",

    @ColumnInfo(name ="score")
    var score: Long = 0, // Change the type to Long

    @ColumnInfo(name ="list_soal")
    val list_soal: List<Soal> = emptyList()
)

data class Soal(

    @ColumnInfo(name = "body")
    val body : String = "",

    @ColumnInfo(name = "image")
    val image : String = "",

    @ColumnInfo(name = "options")
    val options : List<Options> = emptyList()
)

data class Options(

    @ColumnInfo(name = "answer")
    val answer: String = "",

    @ColumnInfo(name = "correct_answer")
    @field:SerializedName("correct_answer")
    val correct_answer: Boolean = false,

    @ColumnInfo(name = "is_click")
    @field:SerializedName("is_click")
    var is_click: Boolean = false,

    @ColumnInfo(name = "option")
    val option: String = ""
)