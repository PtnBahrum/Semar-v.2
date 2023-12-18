package com.example.semarv2.features.learning

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.semarv2.databinding.ActivityMateriListBinding
import com.example.semarv2.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MateriListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMateriListBinding
    private val viewModel : MateriViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToHome.setOnClickListener { backToHome() }

        showListMateri()
    }

    private fun backToHome(){
        onBackPressedDispatcher.onBackPressed()
    }

    private fun showListMateri() {
        binding.rvMateri.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewModel.getMateriList().observe(this) { result ->
            when (result) {
                is Result.Loading -> binding.pbMateri.visibility = View.VISIBLE

                is Result.Success -> {
                    val materiListAdapter = MateriAdapter(result.data)
                    binding.rvMateri.adapter = materiListAdapter
                    binding.pbMateri.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.pbMateri.visibility = View.GONE
                    Log.d(TAG, result.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showListMateri()
    }
}