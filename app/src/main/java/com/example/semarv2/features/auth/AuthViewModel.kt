package com.example.semarv2.features.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semarv2.data.source.remote.repository.AuthRepository
import com.example.semarv2.data.source.remote.model.User
import com.example.semarv2.util.Resource
import com.example.semarv2.util.networkState.AuthState
import com.example.semarv2.util.networkState.ForgotPasswordState
import com.example.semarv2.util.networkState.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel  @Inject constructor(
    val authRepository: AuthRepository
): ViewModel() {

    private val _user = MutableStateFlow(AuthState())
    val user: StateFlow<AuthState> = _user

    private val _userData = MutableStateFlow(UserState())
    val userData: StateFlow<UserState> = _userData

    private val _resetPasswordResult = MutableStateFlow(ForgotPasswordState())
    val resetPasswordResult: StateFlow<ForgotPasswordState> get() = _resetPasswordResult

    fun login(email: String, password: String) {
        authRepository.login(email, password).onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = AuthState(isLoading = true)
                }
                is Resource.Error -> {
                    _user.value = AuthState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _user.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun register(email: String, password: String, user: User) {
        authRepository.register(email, password, user).onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = AuthState(isLoading = true)
                }
                is Resource.Error -> {
                    _user.value = AuthState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _user.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resetPassword(email: String) {
        authRepository.forgotPassword(email).onEach {
            when(it){
                is Resource.Loading -> {
                    _resetPasswordResult.value = ForgotPasswordState(isLoading = true)
                }
                is Resource.Error -> {
                    _resetPasswordResult.value = ForgotPasswordState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _resetPasswordResult.value = ForgotPasswordState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loggedUser() {
        authRepository.getLoggedUser().onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = AuthState(isLoading = true)
                }
                is Resource.Error -> {
                    _user.value = AuthState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _user.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserData() {
        authRepository.getUserData().onEach {
            when (it) {
                is Resource.Loading -> {
                    _userData.value = UserState(isLoading = true)
                }
                is Resource.Error -> {
                    _userData.value = UserState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _userData.value = UserState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signInWithGoogleAndAddUser(data: Intent, user: User) {
        authRepository.signInWithGoogleAndAddUser(data, user).onEach {
            when (it) {
                is Resource.Success -> {
                    _user.value = AuthState(data = it.data)
                }
                is Resource.Error -> {
                    _user.value = AuthState(error = it.message ?: "")
                }
                is Resource.Loading -> {
                    _user.value = AuthState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

//    fun loginWithGoogle(data : Intent){
//        authRepository.loginWithGoogle(data).onEach {
//            when (it) {
//                is Resource.Success -> {
//                    _user.value = AuthState(data = it.data)
//                }
//                is Resource.Error -> {
//                    _user.value = AuthState(error = it.message ?: "")
//                }
//                is Resource.Loading -> {
//                    _user.value = AuthState(isLoading = true)
//                }
//            }
//        }
//    }


}