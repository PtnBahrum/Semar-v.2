package com.example.semarv2.data.source.remote.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.semarv2.data.source.local.entity.MateriEntity
import com.example.semarv2.data.source.local.room.SemarDatabase
import com.example.semarv2.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class MateriRepository @Inject constructor(
    private val database: SemarDatabase,
    private val fireStoreDatabase: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    fun getListMateri() = liveData {
        emit(Result.Loading)
        if (firebaseAuth.currentUser != null){
            try {
                // Ambil data dari Firestore
                val querySnapshot = fireStoreDatabase.collection("materi").get().await()

                // Mapping data Firestore ke model Wayang
                val materiList = mutableListOf<MateriEntity>()
                for (document in querySnapshot.documents) {
                    val materi: MateriEntity? = document.toObject(MateriEntity::class.java)
                    materi?.let { materiList.add(it) }
                }
                database.materiDao().deleteAllMateri()
                // Simpan data ke Room Database
                database.materiDao().insertMateri(materiList)
            }catch (e : HttpException){
                emit(Result.Error(e.localizedMessage.toString() ?: "Unknown Error"))
                Log.d(ContentValues.TAG,e.localizedMessage)
            } catch (e: IOException) {
                emit(
                    Result.Error(
                        e.localizedMessage.toString() ?: "Check Your Internet Connection"
                    )
                )
                Log.d(ContentValues.TAG,e.localizedMessage)
            } catch (e: Exception) {
                emit(Result.Error(e.localizedMessage.toString() ?: ""))
                Log.d(ContentValues.TAG,e.localizedMessage)
            }
            val localData : LiveData<Result<List<MateriEntity>>> =
                database.materiDao().getListMateri().map { Result.Success(it) }
            emitSource(localData)
        }
    }
    fun getMateriById(id : String) = liveData {
        emit(Result.Loading)
        try {
            val snapshot = fireStoreDatabase.collection("materi").document(id).get().await()
            if(snapshot.exists()){
                val materi : MateriEntity? = snapshot.toObject(MateriEntity::class.java)
                //Simpan data ke Room Database
                database.materiDao().insertMateri(listOf(materi) as List<MateriEntity>)
            }
        }catch (e : HttpException){
            emit(Result.Error(e.localizedMessage.toString() ?: "Unknown Error"))
            Log.d(ContentValues.TAG,e.localizedMessage)
        } catch (e: IOException) {
            emit(
                Result.Error(
                    e.localizedMessage.toString() ?: "Check Your Internet Connection"
                )
            )
            Log.d(ContentValues.TAG,e.localizedMessage)
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage.toString() ?: ""))
            Log.d(ContentValues.TAG,e.localizedMessage)
        }
        val localData : LiveData<Result<MateriEntity>> =
            database.materiDao().getMateriById(id).map { Result.Success(it) }
        emitSource(localData)
    }
}