package com.example.semarv2.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.semarv2.data.source.local.entity.MateriEntity

@Dao
interface MateriDao {

    @Query("SELECT * FROM materi")
    fun getListMateri(): LiveData<List<MateriEntity>>

    @Query("SELECT * FROM materi WHERE `id` = :id")
    fun getMateriById(id: String): LiveData<MateriEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMateri(materi: List<MateriEntity>)

    @Query("DELETE FROM materi")
    suspend fun deleteAllMateri()
}