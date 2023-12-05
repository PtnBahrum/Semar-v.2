package com.example.semarv2.features.wayang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semarv2.data.source.local.entity.WayangEntity
import com.example.semarv2.databinding.ActivityWayangListBinding
import com.example.semarv2.features.videos.VideoAdapter
import com.example.semarv2.features.videos.VideoListActivity
import com.example.semarv2.features.wayang.detail.WayangDetailActivity
import com.example.semarv2.util.Result
import com.example.semarv2.util.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WayangListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWayangListBinding
    private val viewModel: WayangViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWayangListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToHome.setOnClickListener { backToHome() }

        viewModel.getListWayangData()
        showListWayang()
    }

    private fun backToHome() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun showListWayang() {
        binding.rvWayang.layoutManager = GridLayoutManager(
            this,
            3
        )

        viewModel.getListWayangData().observe(this) { result ->
            when (result) {
                is Result.Loading -> binding.pbWayang.visibility = View.VISIBLE

                is Result.Success -> {
                    val wayangListAdapter = WayangAdapter(result.data)
                    binding.rvWayang.adapter = wayangListAdapter
                    binding.pbWayang.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.pbWayang.visibility = View.GONE
                    Log.d(TAG, result.toString())
                }
            }
        }
    }

//    private fun showListWayang() {
//        binding.rvWayang.layoutManager = GridLayoutManager(
//            this,
//            3
//        )
//        lifecycle.coroutineScope.launchWhenCreated {
//            viewModel.wayangListData.collect {
//                if (it.isLoading) {
//                    binding.pbWayang.visibility = View.VISIBLE
//                }
//                if (it.error.isNotBlank()) {
//                    binding.pbWayang.visibility = View.GONE
//                    this@WayangListActivity.displayToast(it.error)
//                }
//                it.data?.let { wayangList ->
//                    binding.pbWayang.visibility = View.GONE
//                    // Provide the wayangList data to the adapter
//                    val wayangAdapter = WayangAdapter(wayangList)
//                    binding.rvWayang.adapter = wayangAdapter
//                }
//            }
//        }
//        viewModel.getListVideoData().observe(this) { result ->
//            when (result) {
//                is Result.Loading -> binding.pbVideo.visibility = View.VISIBLE
//
//                is Result.Success -> {
//                    val videoListAdapter = VideoAdapter(result.data)
//                    binding.rvVideo.adapter = videoListAdapter
//                    binding.pbVideo.visibility = View.GONE
//                }
//
//                is Result.Error -> {
//                    binding.pbVideo.visibility = View.GONE
//                    Log.d(VideoListActivity.TAG, result.toString())
//                }
//            }
//        }
//    }

//    private fun observeWayangData() {
//        lifecycle.coroutineScope.launchWhenCreated {
//            viewModel.wayangData.collect { state ->
//                when {
//                    state.isLoading -> {
//                        binding.pbWayang.visibility = View.VISIBLE
//                    }
//                    state.error.isNotBlank() -> {
//                        binding.pbWayang.visibility = View.GONE
//                        displayToast(state.error)
//                    }
//                    state.data != null -> {
//                        binding.pbWayang.visibility = View.GONE
//                        wayangAdapter.updateData(state.data)
//                    }
//                }
//            }
//        }
//    }
    companion object {
        private const val TAG = "WayangList"
    }
}