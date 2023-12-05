package com.example.semarv2.features.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semarv2.data.source.remote.repository.VideoRepository
import com.example.semarv2.data.source.local.entity.VideoEntity
import com.example.semarv2.util.Resource
import com.example.semarv2.util.networkState.VideoListState
import com.example.semarv2.util.networkState.VideoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel(){

    fun getListVideoData() = videoRepository.getListVideo()
    fun getVideoById(id : String) = videoRepository.getVideoById(id)
//    private val _videoListData = MutableStateFlow(VideoListState())
//    val videoListData: StateFlow<VideoListState> = _videoListData
//
//    private val _videoData = MutableStateFlow(VideoState())
//    val videoData: StateFlow<VideoState> = _videoData
//
//    fun getListVideoData() {
//        videoRepository.getListVideo().onEach {
//            when (it) {
//                is Resource.Loading -> {
//                    _videoListData.value = VideoListState(isLoading = true)
//                }
//
//                is Resource.Error -> {
//                    _videoListData.value = VideoListState(error = it.message ?: "")
//                }
//
//                is Resource.Success -> {
//                    _videoListData.value = VideoListState(data = it.data)
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    fun getVideoById(id : String){
//        videoRepository.getVideoById(id).onEach {
//            when (it) {
//                is Resource.Loading -> {
//                    _videoData.value = VideoState(isLoading = true)
//                }
//
//                is Resource.Error -> {
//                    _videoData.value = VideoState(error = it.message ?: "")
//                }
//
//                is Resource.Success -> {
//                    _videoData.value = VideoState(data = flowOf(it.data))
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
}