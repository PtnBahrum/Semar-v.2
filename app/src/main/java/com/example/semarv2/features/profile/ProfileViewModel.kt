package com.example.semarv2.features.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.semarv2.data.source.remote.repository.UserRepository
import com.example.semarv2.util.Resource
import com.example.semarv2.util.networkState.ChangePasswordState
import com.example.semarv2.util.networkState.UpdateProfilState
import com.example.semarv2.util.networkState.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val userRepository: UserRepository
): ViewModel() {

    private val _userData = MutableStateFlow(UserState())
    val userData: StateFlow<UserState> = _userData

    private val _updateProfilResult = MutableStateFlow(UpdateProfilState())
    val updateProfilResult: StateFlow<UpdateProfilState> get() = _updateProfilResult

    private val _updatePasswordResult = MutableStateFlow(ChangePasswordState())
    val updatePasswordResult: StateFlow<ChangePasswordState> get() = _updatePasswordResult

    private val _isLoggedInWithGoogle = MutableStateFlow(false)
    val isLoggedInWithGoogle: StateFlow<Boolean> = _isLoggedInWithGoogle

    init {
        userRepository.isLoggedInWithGoogle().onEach { isLoggedIn ->
            _isLoggedInWithGoogle.value = isLoggedIn
        }.launchIn(viewModelScope)
    }

    fun getUserData() {
        userRepository.getUserData().onEach {
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

    fun logOut() {
        viewModelScope.launch {
            userRepository.logOut()
        }
    }
    fun updateProfil(name : String, imageUri: Uri?){
        userRepository.updateProfile(name, imageUri).onEach {
            when(it){
                is Resource.Loading -> {
                    _updateProfilResult.value = UpdateProfilState(isLoading = true)
                }
                is Resource.Error -> {
                    _updateProfilResult.value = UpdateProfilState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _updateProfilResult.value = UpdateProfilState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun changePassword(oldPassword : String, newPassword: String){
        userRepository.changePassword(oldPassword, newPassword).onEach {
            when(it){
                is Resource.Loading -> {
                    _updatePasswordResult.value = ChangePasswordState(isLoading = true)
                }
                is Resource.Error -> {
                    _updatePasswordResult.value = ChangePasswordState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _updatePasswordResult.value = ChangePasswordState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}