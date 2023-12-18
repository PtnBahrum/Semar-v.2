package com.example.semarv2.data.source.remote.repository

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.semarv2.R
import com.example.semarv2.data.source.remote.model.User
import com.example.semarv2.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.childEvents
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject


class AuthRepository @Inject constructor(){
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val fireStoreDatabase = FirebaseFirestore.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    init {
        if (firebaseAuth.currentUser != null) {
            loggedOutLiveData.postValue(false)
        }
    }

    fun register(email: String, password: String, user: User): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())

        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = firebaseAuth.currentUser!!.uid
            user.id = userId

            firebaseDatabase.reference.child("user").child(userId).setValue(user).await()
            firebaseDatabase.reference.child("quiz").child("quiz_1").child("score")
                .child(userId).setValue(0).await()
            firebaseDatabase.reference.child("quiz").child("quiz_2").child("score")
                .child(userId).setValue(0).await()
            firebaseDatabase.reference.child("quiz").child("quiz_3").child("score")
                .child(userId).setValue(0).await()
            firebaseDatabase.reference.child("quiz").child("quiz_4").child("score")
                .child(userId).setValue(0).await()

            emit((result.user?.let {
                Resource.Success(data = it)
            }!!))

            loggedOutLiveData.postValue(true)
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }

    fun login(email: String, password: String): Flow<Resource<FirebaseUser>> = flow {

        emit(Resource.Loading())

        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit((result.user?.let {
                Resource.Success(data = it)
            }!!))
            loggedOutLiveData.postValue(false)

        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }

    }

    fun forgotPassword(email : String, context: Context): Flow<Resource<Unit>> = flow{
        emit(Resource.Loading())

        try {
            // Buat query untuk mencari user dengan email yang sesuai
            val snapQuery = firebaseDatabase.reference.child("user").orderByChild("email").equalTo(email).get().await()

            if (snapQuery.exists()) {
                firebaseAuth.sendPasswordResetEmail(email).await()
                emit(Resource.Success(data = Unit))
                loggedOutLiveData.postValue(true)
            } else {
                emit(Resource.Error(message = context.resources.getString(R.string.email_invalid_error)))
            }
        }catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
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
//                val snapshot = fireStoreDatabase.collection("User")
//                    .document(firebaseAuth.currentUser!!.uid).get().await()
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

    fun signInWithGoogleAndAddUser(data: Intent, user: User): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())

        try {
            // Get the Google account from the intent
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)

            // Sign in with Google credentials
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()

            // Check if the user already exists in the Firebase Database
            val userId = firebaseAuth.currentUser?.uid
            val userSnapshot = firebaseDatabase.reference.child("user").child(userId!!).get().await()

            if (!userSnapshot.exists()) {
                // Add the user data to the Firebase Database if the user doesn't exist
                user.id = userId
                firebaseDatabase.reference.child("user").child(userId).setValue(user).await()
                // You can add additional data or initialization as needed
            }

            emit((result.user?.let {
                Resource.Success(data = it)
            }!!))
            loggedOutLiveData.postValue(false)

        } catch (e: ApiException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Google Sign-In failed"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }
}