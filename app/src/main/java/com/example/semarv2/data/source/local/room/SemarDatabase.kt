package com.example.semarv2.data.source.local.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.semarv2.data.source.local.entity.VideoEntity
import com.example.semarv2.data.source.local.entity.WayangEntity
import com.example.semarv2.data.source.local.entity.KuisEntity
import com.example.semarv2.data.source.local.entity.MateriEntity
import com.example.semarv2.util.Converters
import com.example.semarv2.util.KuisConverter

@Database(
    entities = [WayangEntity::class, VideoEntity::class, KuisEntity::class, MateriEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class, KuisConverter::class)
abstract class SemarDatabase : RoomDatabase(){

    abstract fun wayangDao() : WayangDao
    abstract fun videoDao() : VideoDao
    abstract fun kuisDao() : KuisDao
    abstract fun materiDao() : MateriDao

    companion object {
        @Volatile
        private var INSTANCE: SemarDatabase? = null

        fun getDatabase(context: Context): SemarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SemarDatabase::class.java,
                    "wayang_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}