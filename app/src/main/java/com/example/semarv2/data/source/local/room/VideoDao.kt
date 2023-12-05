package com.example.semarv2.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.semarv2.data.source.local.entity.VideoEntity

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos")
    fun getListVideo(): LiveData<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE `id` = :id")
    fun getVideoById(id: String): LiveData<VideoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Query("DELETE FROM videos")
    suspend fun deleteAllVideos()
}