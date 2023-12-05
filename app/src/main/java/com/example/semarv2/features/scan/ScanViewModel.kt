package com.example.semarv2.features.scan

import androidx.lifecycle.ViewModel
import com.example.semarv2.data.source.remote.repository.WayangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val wayangRepository: WayangRepository
) : ViewModel(){

    fun predictWayang(imgMultipart: MultipartBody.Part) = wayangRepository.predictWayang(imgMultipart)
}