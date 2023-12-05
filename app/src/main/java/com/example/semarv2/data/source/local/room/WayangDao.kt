package com.example.semarv2.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.semarv2.data.source.remote.model.Wayang
import com.example.semarv2.data.source.local.entity.WayangEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WayangDao {

    @Query("SELECT * FROM wayangs")
    fun getListWayang(): LiveData<List<WayangEntity>>

    @Query("SELECT * FROM wayangs WHERE `id` = :id")
    fun getWayangById(id: String): LiveData<WayangEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWayangs(wayangs: List<WayangEntity>)

    @Query("DELETE FROM wayangs")
    suspend fun deleteAllWayangs()
}