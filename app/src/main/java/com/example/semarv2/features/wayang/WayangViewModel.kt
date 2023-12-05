package com.example.semarv2.features.wayang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semarv2.data.source.remote.repository.WayangRepository
import com.example.semarv2.util.Resource
import com.example.semarv2.util.networkState.UserState
import com.example.semarv2.util.networkState.WayangListState
import com.example.semarv2.util.networkState.WayangState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WayangViewModel @Inject constructor(
    private val wayangRepository: WayangRepository
    ) : ViewModel() {
    fun getListWayangData() = wayangRepository.getListWayang()
    fun getWayangById(id : String) = wayangRepository.getWayangById(id)
//    private val _wayangListData = MutableStateFlow(WayangListState())
//    val wayangListData: StateFlow<WayangListState> = _wayangListData
//
//    private val _wayangData = MutableStateFlow(WayangState())
//    val wayangData: StateFlow<WayangState> = _wayangData
//
//    fun getListWayangData() {
//        wayangRepository.getListWayang().onEach {
//            when (it) {
//                is Resource.Loading -> {
//                    _wayangListData.value = WayangListState(isLoading = true)
//                }
//
//                is Resource.Error -> {
//                    _wayangListData.value = WayangListState(error = it.message ?: "")
//                }
//
//                is Resource.Success -> {
//                    _wayangListData.value = WayangListState(data = it.data)
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    fun getWayangByid(id : String){
//        wayangRepository.getWayangById(id).onEach {
//            when(it){
//                is Resource.Loading -> {
//                    _wayangData.value = WayangState(isLoading = true)
//                }
//
//                is Resource.Error -> {
//                    _wayangData.value = WayangState(error = it.message ?: "")
//                }
//
//                is Resource.Success -> {
//                    _wayangData.value = WayangState(data = it.data)
//                }
//            }
//        }.launchIn(viewModelScope)
//    }

//    private val _wayangData = MutableStateFlow(WayangListState())
//    val wayangData: StateFlow<WayangListState> = _wayangData
//
//    fun getListWayangData() {
//        wayangRepository.getListWayang().onEach {
//            when (it) {
//                is Resource.Loading -> {
//                    _wayangData.value = WayangListState(isLoading = true)
//                }
//
//                is Resource.Error -> {
//                    _wayangData.value = WayangListState(error = it.message ?: "")
//                }
//
//                is Resource.Success -> {
//                    _wayangData.value = WayangListState(data = it.data)
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
}