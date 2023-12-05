package com.example.semarv2.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semarv2.data.source.remote.repository.UserRepository
import com.example.semarv2.util.Resource
import com.example.semarv2.util.networkState.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    val userRepository: UserRepository
): ViewModel() {

    private val _userData = MutableStateFlow(UserState())
    val userData: StateFlow<UserState> = _userData

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
}