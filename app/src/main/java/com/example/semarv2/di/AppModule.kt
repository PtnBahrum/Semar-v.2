package com.example.semarv2.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.semarv2.data.source.local.room.KuisDao
import com.example.semarv2.data.source.remote.repository.AuthRepository
import com.example.semarv2.data.source.remote.repository.UserRepository
import com.example.semarv2.data.source.remote.repository.VideoRepository
import com.example.semarv2.data.source.remote.repository.WayangRepository
import com.example.semarv2.data.source.local.room.SemarDatabase
import com.example.semarv2.data.source.local.room.VideoDao
import com.example.semarv2.data.source.local.room.WayangDao
import com.example.semarv2.data.source.remote.network.retrofit.ApiConfig
import com.example.semarv2.data.source.remote.network.retrofit.ApiService
import com.example.semarv2.data.source.remote.repository.KuisRepository
import com.example.semarv2.data.source.remote.repository.MateriRepository
import com.example.semarv2.features.profile.SettingPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun provideAuthRepository() = AuthRepository()

    @Provides
    @Singleton
    fun provideUserRepository() = UserRepository()

    @Provides
    @Singleton
    fun provideWayangRepository(
        database: SemarDatabase,
        fireStoreDatabase: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        apiService: ApiService
    ) : WayangRepository {
        return WayangRepository(database, fireStoreDatabase, firebaseAuth, apiService)
    }

    @Provides
    @Singleton
    fun provideVideoRepository(
        database: SemarDatabase,
        fireStoreDatabase: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : VideoRepository {
        return VideoRepository(database, fireStoreDatabase, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideMateriRepository(
        database: SemarDatabase,
        fireStoreDatabase: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : MateriRepository {
        return MateriRepository(database, fireStoreDatabase, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideKuisRepository(
        database: SemarDatabase,
        databaseFirebase : FirebaseDatabase,
        firebaseAuth: FirebaseAuth
    ): KuisRepository{
        return KuisRepository(database, databaseFirebase, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideSemarDatabase(@ApplicationContext appContext: Context): SemarDatabase {
        return SemarDatabase.getDatabase(appContext)
    }

    @Provides
    fun provideWayangDao(semarDatabase: SemarDatabase): WayangDao {
        return semarDatabase.wayangDao()
    }

    @Provides
    @Singleton
    fun provideSettingPreference(
        dataStore: DataStore<Preferences>
    ): SettingPreference {
        return SettingPreference.getInstance(dataStore)
    }

    @Provides
    fun provideKuisDao(semarDatabase: SemarDatabase): KuisDao {
        return semarDatabase.kuisDao()
    }

    @Provides
    fun provideVideoDao(semarDatabase: SemarDatabase): VideoDao {
        return semarDatabase.videoDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStoreInstance(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideApiServiceInstance(): ApiService {
        return ApiConfig.getApiService()
    }
}