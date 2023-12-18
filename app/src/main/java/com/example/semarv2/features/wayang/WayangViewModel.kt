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
}