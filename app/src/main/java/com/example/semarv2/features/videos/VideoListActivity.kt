package com.example.semarv2.features.videos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.example.semarv2.util.Result
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semarv2.databinding.ActivityVideoListBinding
import com.example.semarv2.features.videos.detail.VideoDetailActivity
import com.example.semarv2.util.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVideoListBinding
    private val viewModel: VideoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToHome.setOnClickListener { backToHome() }

        showListVideo()
    }

    private fun backToHome(){
        onBackPressedDispatcher.onBackPressed()
    }
    private fun showListVideo() {
        binding.rvVideo.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewModel.getListVideoData().observe(this) { result ->
            when (result) {
                is Result.Loading -> binding.pbVideo.visibility = View.VISIBLE

                is Result.Success -> {
                    val videoListAdapter = VideoAdapter(result.data)
                    binding.rvVideo.adapter = videoListAdapter
                    binding.pbVideo.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.pbVideo.visibility = View.GONE
                    Log.d(TAG, result.toString())
                }
            }
        }
    }

//    private fun observeWayangData() {
//        lifecycle.coroutineScope.launchWhenCreated {
//            viewModel.videoListData.collect { state ->
//                when {
//                    state.isLoading -> {
//                        binding.pbVideo.visibility = View.VISIBLE
//                    }
//                    state.error.isNotBlank() -> {
//                        binding.pbVideo.visibility = View.GONE
//                        displayToast(state.error)
//                    }
//                    state.data != null -> {
//                        binding.pbVideo.visibility = View.GONE
//                        videoAdapter.updateData(state.data)
//                    }
//                }
//            }
//        }
//    }
    companion object {
        const val TAG = "VideoList"
    }

}