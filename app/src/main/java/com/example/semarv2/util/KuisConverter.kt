package com.example.semarv2.util

import androidx.room.TypeConverter
import com.example.semarv2.data.source.local.entity.Options
import com.example.semarv2.data.source.local.entity.Soal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class KuisConverter {
    @TypeConverter
    fun fromSoalList(value: List<Soal>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toSoalList(value: String): List<Soal> {
        val listType = object : TypeToken<List<Soal>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromOptionsList(value: List<Options>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toOptionsList(value: String): List<Options> {
        val listType = object : TypeToken<List<Options>>() {}.type
        return Gson().fromJson(value, listType)
    }
}