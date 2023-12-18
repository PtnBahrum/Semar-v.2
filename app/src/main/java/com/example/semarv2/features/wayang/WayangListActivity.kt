package com.example.semarv2.features.wayang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.semarv2.databinding.ActivityWayangListBinding
import com.example.semarv2.util.Result
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
        viewModel.getListWayangData().observe(this) { result ->
            when (result) {
                is Result.Loading -> binding.pbWayang.visibility = View.VISIBLE

                is Result.Success -> {
                    val wayangListAdapter = WayangAdapter(result.data)
                    binding.gvWayang.adapter = wayangListAdapter
                    binding.pbWayang.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.pbWayang.visibility = View.GONE
                    Log.d(TAG, result.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showListWayang()
    }
    companion object {
        private const val TAG = "WayangList"
    }
}