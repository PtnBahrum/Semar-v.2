package com.example.semarv2.data.source.remote.model

import androidx.room.Entity

data class Kuis(
    val id  : Long,
    val title : String,
    val image : String,
    val score : Long,
    val list_soal : List<Soal>
)

data class Soal(
    val body : String,
    val image : String = "",
    val options : List<Options>
)

data class Options(
    val answer : String,
    val correct_answer : String,
    val is_click : String,
    val option : String,
)