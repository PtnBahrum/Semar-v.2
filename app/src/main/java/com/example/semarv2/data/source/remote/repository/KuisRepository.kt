package com.example.semarv2.data.source.remote.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.semarv2.data.source.local.entity.KuisEntity
import com.example.semarv2.data.source.local.entity.Options
import com.example.semarv2.data.source.local.entity.Soal
import com.example.semarv2.data.source.local.room.SemarDatabase
import com.example.semarv2.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.semarv2.util.Result
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class KuisRepository @Inject constructor(
    private val database: SemarDatabase,
    private val databaseFirebase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) {
    fun getListKuis() = liveData {
        emit(Result.Loading)

        if (firebaseAuth.currentUser != null) {
            try {
                // Retrieve the current user ID
                val currentUser = firebaseAuth.currentUser!!.uid
                Log.d(ContentValues.TAG, "Fetching quiz data for user: $currentUser")

                // Initialize an empty list for quiz entities
                val kuisList = mutableListOf<KuisEntity>()

                // Parse the JSON data
                val databaseReference = databaseFirebase.getReference("quiz")
                val documentSnapshot = databaseReference.get().await()

                for (document in documentSnapshot.children) {
                    val quizData = document.value as Map<String, Any>

                    // Extract quiz information
                    val id = quizData["id"] as Long
                    val title = quizData["title"] as String
                    val image = quizData["image"] as String

                    // Extract score for the current user
                    val scoreMap = quizData["score"] as Map<String, Long>
                    val score = scoreMap[currentUser] ?: 0

                    // Extract list of soal entities
                    val listSoal = mutableListOf<Soal>()
                    for (soalData in quizData["list_soal"] as List<Map<String, Any>>) {
                        val body = soalData["body"] as String
                        val image = soalData["image"] as String

                        val options = mutableListOf<Options>()
                        for (optionData in soalData["options"] as List<Map<String, Any>>) {
                            val answer = optionData["answer"] as String
                            val correctAnswer = optionData["correct_answer"] as Boolean
                            val isClick = optionData["is_click"] as Boolean
                            val option = optionData["option"] as String

                            options.add(Options(answer, correctAnswer, isClick, option))
                        }

                        listSoal.add(Soal(body, image, options))
                    }

                    // Create and add kuis entity to the list
                    val kuis = KuisEntity(id, title, image, score, listSoal)
                    kuisList.add(kuis)

                    Log.d(ContentValues.TAG, "Extracted quiz information: $kuis")
                }

                // Emit the list of quiz entities with success status
                emit(Result.Success(kuisList))

                // Clear local quiz data
                database.kuisDao().deleteAllKuis()
                Log.d(ContentValues.TAG, "Cleared local quiz data")

                // Save data to Room Database
                database.kuisDao().insertkuis(kuisList)
                Log.d(ContentValues.TAG, "Saved quiz data to local database: $kuisList")
            } catch (e: HttpException) {
                emit(Result.Error(e.localizedMessage.toString() ?: "Unknown Error"))
                Log.d(ContentValues.TAG, "Error fetching quiz data: ${e.localizedMessage}")
            } catch (e: IOException) {
                emit(
                    Result.Error(
                        e.localizedMessage.toString() ?: "Check Your Internet Connection"
                    )
                )
                Log.d(ContentValues.TAG, "Network error: ${e.localizedMessage}")
            } catch (e: Exception) {
                emit(Result.Error(e.localizedMessage.toString() ?: ""))
                Log.d(ContentValues.TAG, "Unexpected error: ${e.localizedMessage}")
            }

            // Emit the local quiz data stream
            val localData: LiveData<Result<List<KuisEntity>>> =
                database.kuisDao().getListKuis().map { Result.Success(it) }
            emitSource(localData)
        }
    }
//    fun getListKuis() = liveData {
//        emit(Result.Loading)
//
//        if (firebaseAuth.currentUser != null) {
//            try {
//                // Retrieve the current user ID
//                val currentUser = firebaseAuth.currentUser!!.uid
//                Log.d(ContentValues.TAG, "Fetching quiz data for user: $currentUser")
//
//                // Fetch quiz data from Firebase Realtime Database
//                val querySnapshot = databaseFirebase.getReference("quiz").get().await()
//                Log.d(ContentValues.TAG, "Retrieved quiz data: $querySnapshot")
//
//                // Initialize an empty list for quiz entities
//                val kuisList = mutableListOf<KuisEntity>()
//
//                // Process each quiz document from the query snapshot
//                for (document in querySnapshot.children) {
//                    val quizData: Map<String, Any>? = document.value as? Map<String, Any>
//                    Log.d(ContentValues.TAG, "Processing quiz document: $document")
//
//                    // Extract quiz information from the document
//                    val kuis = KuisEntity(
//                        id = quizData!!["id"] as Long,
//                        title = quizData["title"] as String,
//                        image = quizData["image"] as String,
//                        score = 0,
//                        list_soal = emptyList()
//                    )
//                    Log.d(ContentValues.TAG, "Extracted quiz information: $kuis")
//
//                    // Retrieve and store score for the current user
//                    if (quizData.containsKey("score")) {
//                        val scoreMap = quizData["score"] as Map<String, Long>
//                        Log.d(ContentValues.TAG, "Retrieving score data: $scoreMap")
//
//                        if (scoreMap.containsKey(currentUser)) {
//                            kuis.score = scoreMap[currentUser]!!.toLong()
//                            Log.d(ContentValues.TAG, "Storing score for user $currentUser: ${kuis.score}")
//                        }
//                    }
//
//                    // Add the quiz entity to the list
//                    kuisList.add(kuis)
//                }
//
//                // Emit the list of quiz entities with success status
//                emit(Result.Success(kuisList))
//
//                // Clear local quiz data
//                database.kuisDao().deleteAllKuis()
//                Log.d(ContentValues.TAG, "Cleared local quiz data")
//
//                // Save data to Room Database
//                database.kuisDao().insertkuis(kuisList)
//                Log.d(ContentValues.TAG, "Saved quiz data to local database: $kuisList")
//            } catch (e: HttpException) {
//                emit(Result.Error(e.localizedMessage.toString() ?: "Unknown Error"))
//                Log.d(ContentValues.TAG, "Error fetching quiz data: ${e.localizedMessage}")
//            } catch (e: IOException) {
//                emit(
//                    Result.Error(
//                        e.localizedMessage.toString() ?: "Check Your Internet Connection"
//                    )
//                )
//                Log.d(ContentValues.TAG, "Network error: ${e.localizedMessage}")
//            } catch (e: Exception) {
//                emit(Result.Error(e.localizedMessage.toString() ?: "Unexpected error"))
//                Log.d(ContentValues.TAG, "Unexpected error: ${e.localizedMessage}")
//            }
//
//            // Emit the local quiz data stream
//            val localData: LiveData<Result<List<KuisEntity>>> =
//                database.kuisDao().getListKuis().map { Result.Success(it) }
//            emitSource(localData)
//        }
//    }

    fun getKuisById(id : Long) = liveData {
        emit(Result.Loading)
        if(firebaseAuth.currentUser != null){
            try {
                val snapshot = databaseFirebase.getReference("quiz").child("quiz_${id}").get().await()
                if (snapshot.exists()) {
                    val kuis: KuisEntity? = snapshot.getValue(KuisEntity::class.java)
                    database.kuisDao().insertkuis(listOf(kuis) as List<KuisEntity>)
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

            val localData : LiveData<Result<KuisEntity>> =
                database.kuisDao().getKuisById(id).map { Result.Success(it) }
            emitSource(localData)
        }
    }

//    fun updateScore(score : Int, id : Int) = liveData {
//        emit(Result.Loading)
//        if(firebaseAuth.currentUser != null){
//            try {
//                val snapQuery = databaseFirebase.getReference("user").child(firebaseAuth.currentUser!!.uid)
//                snapQuery.child("score_quiz_${id}").setValue(score).await()
//
//                val queryDataQuiz = databaseFirebase.getReference("quiz").child("quiz_${id}").child("score")
//                queryDataQuiz.child(firebaseAuth.currentUser!!.uid).setValue(score).await()
//
//            } catch (e : HttpException){
//                emit(Result.Error(e.localizedMessage.toString() ?: "Unknown Error"))
//                Log.d(ContentValues.TAG,e.localizedMessage)
//            } catch (e: IOException) {
//                emit(
//                    Result.Error(
//                        e.localizedMessage.toString() ?: "Check Your Internet Connection"
//                    )
//                )
//                Log.d(ContentValues.TAG,e.localizedMessage)
//            } catch (e: Exception) {
//                emit(Result.Error(e.localizedMessage.toString() ?: ""))
//                Log.d(ContentValues.TAG,e.localizedMessage)
//            }
//
//            val localData : LiveData<Result<List<KuisEntity>>> =
//                database.kuisDao().getListKuis().map { Result.Success(it) }
//            emitSource(localData)
//        }
//    }

    fun updateQuizScore(score : Long, id : Long): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val snapQuery = databaseFirebase.reference.child("user").child(firebaseAuth.currentUser!!.uid)
            snapQuery.child("score_quiz_${id}").setValue(score).await()

            val queryRef = databaseFirebase.reference.child("quiz").child("quiz_${id}").child("score")
            queryRef.child(firebaseAuth.currentUser!!.uid).setValue(score).await()

            emit(Resource.Success(data = Unit))

        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "Check Your Internet Connection"
                )
            )
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }
}