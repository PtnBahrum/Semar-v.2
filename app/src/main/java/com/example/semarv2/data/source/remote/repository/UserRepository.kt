package com.example.semarv2.data.source.remote.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.semarv2.data.source.remote.model.User
import com.example.semarv2.util.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(){
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val fireStoreDatabase = FirebaseFirestore.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val storage = Firebase.storage
    init {
        if (firebaseAuth.currentUser != null) {
            loggedOutLiveData.postValue(false)
        }
    }
    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    fun getLoggedUser(): Flow<Resource<FirebaseUser>> = flow {

        emit(Resource.Loading())

        if (firebaseAuth.currentUser != null) {
            loggedOutLiveData.postValue(false)
            emit(Resource.Success(data = firebaseAuth.currentUser!!))
        } else {
            emit(Resource.Error("Not Logged"))
        }

    }

    fun getUserData(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        if (firebaseAuth.currentUser != null) {
            try {
                val snapshot = firebaseDatabase.getReference().child("user").child(firebaseAuth.currentUser!!.uid).get().await()
                if (snapshot.exists()) {
                    val user: User? = snapshot.getValue<User>(User::class.java)
                    emit(Resource.Success(data = user!!))
                }
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

    fun updateProfile(name: String = "", imageUri: Uri?): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        try {
            // Update nama jika tidak kosong
            if (name != "") {
                val userRef = firebaseDatabase.reference.child("user").child(firebaseAuth.currentUser!!.uid)
                userRef.child("name").setValue(name)

                // Update nama di FirebaseAuth
                val userUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                firebaseAuth.currentUser?.updateProfile(userUpdate)?.await()
            }

            // Update foto profil jika URI tidak null
            imageUri?.let { uri ->
                // Mendapatkan referensi ke Firebase Storage
                val folderName = "profile_images"
                val imageName = "${firebaseAuth.currentUser!!.uid}.jpg"
                val storageRef = storage.reference.child("$folderName/$imageName")

                // Mengunggah file gambar
                storageRef.putFile(uri).await()

                // Mengambil URL gambar setelah diunggah
                val imageUrl = storageRef.downloadUrl.await().toString()

                // Simpan link gambar ke Realtime Database
                val userRef = firebaseDatabase.reference.child("user").child(firebaseAuth.currentUser!!.uid)
                userRef.child("image").setValue(imageUrl)

                // Update foto di FirebaseAuth
                val userUpdate = UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build()

                firebaseAuth.currentUser?.updateProfile(userUpdate)?.await()
            }

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

    fun changePassword(oldPassword: String, newPassword: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        try {
            val user = firebaseAuth.currentUser

            // Pastikan pengguna sudah login
            if (user != null) {
                // Autentikasi pengguna untuk memastikan oldPassword benar
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
                user.reauthenticate(credential).await()

                // Update password
                user.updatePassword(newPassword).await()

                emit(Resource.Success(data = Unit))
            } else {
                emit(Resource.Error(message = "User not logged in"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }

    fun isLoggedInWithGoogle(): Flow<Boolean> {
        return flow {
            val user = firebaseAuth.currentUser
            emit(user != null && user.providerData.any { it.providerId == "google.com" })
        }
    }
}