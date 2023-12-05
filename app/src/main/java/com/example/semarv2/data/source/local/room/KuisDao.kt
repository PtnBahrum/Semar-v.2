package com.example.semarv2.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.semarv2.data.source.local.entity.KuisEntity

@Dao
interface KuisDao {
    @Query("SELECT * FROM kuis")
    fun getListKuis(): LiveData<List<KuisEntity>>

    @Query("SELECT * FROM kuis WHERE `id` = :id")
    fun getKuisById(id: Long): LiveData<KuisEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertkuis(kuis: List<KuisEntity>)

    @Query("DELETE FROM kuis")
    suspend fun deleteAllKuis()
}