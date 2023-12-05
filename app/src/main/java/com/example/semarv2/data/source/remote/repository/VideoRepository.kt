package com.example.semarv2.data.source.remote.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.semarv2.data.source.local.entity.VideoEntity
import com.example.semarv2.data.source.local.room.SemarDatabase
import com.example.semarv2.util.Resource
import com.example.semarv2.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val database: SemarDatabase,
    private val fireStoreDatabase: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
//    fun getListVideo() = flow {
//        emit(Resource.Loading())
//        if (firebaseAuth.currentUser != null) {
//            try {
//                // Ambil data dari Firestore
//                val querySnapshot = fireStoreDatabase.collection("wayang_video").get().await()
//
//                // Mapping data Firestore ke model Wayang
//                val videoList = mutableListOf<VideoEntity>()
//                for (document in querySnapshot.documents) {
//                    val video: VideoEntity? = document.toObject(VideoEntity::class.java)
//                    video?.let { videoList.add(it) }
//                }
//
//                // Simpan data ke Room Database
//                withContext(Dispatchers.IO) {
//                    database.videoDao().insertVideos(videoList)
//                }
//
//                // Ambil data dari Room Database
//                val localData = withContext(Dispatchers.IO) {
//                    database.videoDao().getListVideo()
//                }
//
//                // Emit data ke flow
//                emit(Resource.Success(data = localData))
//
//            } catch (e: HttpException) {
//                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
//                Log.d(TAG,e.localizedMessage)
//            } catch (e: IOException) {
//                emit(
//                    Resource.Error(
//                        message = e.localizedMessage ?: "Check Your Internet Connection"
//                    )
//                )
//                Log.d(TAG,e.localizedMessage)
//            } catch (e: Exception) {
//                emit(Resource.Error(message = e.localizedMessage ?: ""))
//                Log.d(TAG,e.localizedMessage)
//            }
//        }
//    }

    fun getListVideo() = liveData {
        emit(Result.Loading)
        if (firebaseAuth.currentUser != null){
            try {
                // Ambil data dari Firestore
                val querySnapshot = fireStoreDatabase.collection("wayang_video").get().await()

                // Mapping data Firestore ke model Wayang
                val videoList = mutableListOf<VideoEntity>()
                for (document in querySnapshot.documents) {
                    val video: VideoEntity? = document.toObject(VideoEntity::class.java)
                    video?.let { videoList.add(it) }
                }
                database.videoDao().deleteAllVideos()
                // Simpan data ke Room Database
                database.videoDao().insertVideos(videoList)
            }catch (e : HttpException){
                emit(Result.Error(e.localizedMessage.toString() ?: "Unknown Error"))
                Log.d(TAG,e.localizedMessage)
            } catch (e: IOException) {
                emit(
                    Result.Error(
                        e.localizedMessage.toString() ?: "Check Your Internet Connection"
                    )
                )
                Log.d(TAG,e.localizedMessage)
            } catch (e: Exception) {
                emit(Result.Error(e.localizedMessage.toString() ?: ""))
                Log.d(TAG,e.localizedMessage)
            }
            val localData : LiveData<Result<List<VideoEntity>>> =
                database.videoDao().getListVideo().map { Result.Success(it) }
            emitSource(localData)
        }
    }

//    fun getVideoById(id : String) = flow {
//        emit(Resource.Loading())
//        if (firebaseAuth.currentUser != null){
//            try {
//                val snapshot = fireStoreDatabase.collection("wayang_video").document(id).get().await()
//                if(snapshot.exists()){
//                    val video: VideoEntity? = snapshot.toObject(VideoEntity::class.java)
//
//                    // Simpan data ke Room Database
//                    withContext(Dispatchers.IO) {
//                        database.videoDao().insertVideos(listOf(video) as List<VideoEntity>)
//                    }
//
//                    // Ambil data dari Room Database
//                    val localData = withContext(Dispatchers.IO) {
//                        database.videoDao().getVideoById(id)
//                    }
//                    // Emit data ke flow
//                    emit(Resource.Success(data = localData))
//                }
//            }catch (e : HttpException){
//                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
//                Log.d(TAG,e.localizedMessage)
//            } catch (e: IOException) {
//                emit(
//                    Resource.Error(
//                        message = e.localizedMessage ?: "Check Your Internet Connection"
//                    )
//                )
//                Log.d(TAG,e.localizedMessage)
//            } catch (e: Exception) {
//                emit(Resource.Error(message = e.localizedMessage ?: ""))
//                Log.d(TAG,e.localizedMessage)
//            }
//        }
//    }

    fun getVideoById(id : String) = liveData {
        emit(Result.Loading)
        try {
            val snapshot = fireStoreDatabase.collection("wayang_video").document(id).get().await()
            if(snapshot.exists()){
                val video : VideoEntity? = snapshot.toObject(VideoEntity::class.java)
                //Simpan data ke Room Database
                database.videoDao().insertVideos(listOf(video) as List<VideoEntity>)
            }
        }catch (e : HttpException){
            emit(Result.Error(e.localizedMessage.toString() ?: "Unknown Error"))
            Log.d(TAG,e.localizedMessage)
        } catch (e: IOException) {
            emit(
                Result.Error(
                    e.localizedMessage.toString() ?: "Check Your Internet Connection"
                )
            )
            Log.d(TAG,e.localizedMessage)
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage.toString() ?: ""))
            Log.d(TAG,e.localizedMessage)
        }
        val localData : LiveData<Result<VideoEntity>> =
            database.videoDao().getVideoById(id).map { Result.Success(it) }
        emitSource(localData)
    }
}