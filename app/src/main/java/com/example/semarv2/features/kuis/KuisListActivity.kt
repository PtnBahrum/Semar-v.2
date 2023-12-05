package com.example.semarv2.features.kuis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.semarv2.MainActivity
import com.example.semarv2.R
import com.example.semarv2.databinding.ActivityKuisListBinding
import com.example.semarv2.features.home.HomeFragment
import com.example.semarv2.features.kuis.Adapter.KuisAdapter
import com.example.semarv2.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KuisListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityKuisListBinding
        private val viewModel: KuisViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKuisListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToHome.setOnClickListener { onBackPressed() }

        showListKuis()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
    private fun showListKuis() {
        binding.rvKuis.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewModel.getListKuis().observe(this) { result ->
            when (result) {
                is Result.Loading -> binding.pbKuis.visibility = View.VISIBLE

                is Result.Success -> {
                    val kuisListAdapter = KuisAdapter(result.data)
                    binding.rvKuis.adapter = kuisListAdapter
                    binding.pbKuis.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.pbKuis.visibility = View.GONE
                    Log.d(TAG, result.toString())
                }
            }
        }
    }
    companion object {
        private const val TAG = "KuisListActivity"
    }
}