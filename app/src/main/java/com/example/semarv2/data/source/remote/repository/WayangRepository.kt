package com.example.semarv2.data.source.remote.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.semarv2.data.source.local.entity.WayangEntity
import com.example.semarv2.data.source.local.room.SemarDatabase
import com.example.semarv2.data.source.remote.network.retrofit.ApiService
import com.example.semarv2.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.Dispatchers
import com.example.semarv2.util.Result
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject

class WayangRepository @Inject constructor(
    private val database: SemarDatabase,
    private val fireStoreDatabase: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val apiService: ApiService
){

    fun getListWayang() = liveData {
        emit(Result.Loading)
        if(firebaseAuth.currentUser != null){
            try {
                // Ambil data dari Firestore
                val querySnapshot = fireStoreDatabase.collection("wayang_detail").get().await()

                // Mapping data Firestore ke model Wayang
                val wayangList = mutableListOf<WayangEntity>()
                for (document in querySnapshot.documents) {
                    val wayang: WayangEntity? = document.toObject(WayangEntity::class.java)
                    wayang?.let { wayangList.add(it) }
                }

                database.wayangDao().deleteAllWayangs()
                // Simpan data ke Room Database
                database.wayangDao().insertWayangs(wayangList)
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

            val localData : LiveData<Result<List<WayangEntity>>> =
                database.wayangDao().getListWayang().map { Result.Success(it) }
            emitSource(localData)
        }
    }
    fun getWayangById(id : String) = liveData {
        emit(Result.Loading)
        if( firebaseAuth.currentUser != null){
            try {
                val snapshot = fireStoreDatabase.collection("wayang_detail").document(id).get().await()
                if(snapshot.exists()){
                    val wayang : WayangEntity? = snapshot.toObject(WayangEntity::class.java)
                    //Simpan data ke Room Database
                    database.wayangDao().insertWayangs(listOf(wayang) as List<WayangEntity>)
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
            val localData : LiveData<Result<WayangEntity>> =
                database.wayangDao().getWayangById(id).map { Result.Success(it) }
            emitSource(localData)
        }
    }

    fun predictWayang(imgMultipart: MultipartBody.Part) = liveData {
        emit(Result.Loading)
        try {

            val response = apiService.predictWayang(imgMultipart)
            emit(Result.Success(response))

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
    }
}