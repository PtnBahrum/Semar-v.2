package com.example.semarv2.features.kuis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semarv2.data.source.remote.repository.KuisRepository
import com.example.semarv2.util.Resource
import com.example.semarv2.util.networkState.ChangePasswordState
import com.example.semarv2.util.networkState.UpdateScoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class KuisViewModel @Inject constructor(
   private val kuisRepository: KuisRepository
) : ViewModel(){

    private val _updateScoreResult = MutableStateFlow(UpdateScoreState())
    val updateScoreResult: StateFlow<UpdateScoreState> get() = _updateScoreResult

    fun getListKuis() = kuisRepository.getListKuis()
    fun getKuisById(id : Long) = kuisRepository.getKuisById(id)

    fun updatePassword(score : Long, id: Long){
        kuisRepository.updateQuizScore(score, id).onEach {
            when(it){
                is Resource.Loading -> {
                    _updateScoreResult.value = UpdateScoreState(isLoading = true)
                }
                is Resource.Error -> {
                    _updateScoreResult.value = UpdateScoreState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _updateScoreResult.value = UpdateScoreState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}