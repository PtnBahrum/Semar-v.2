package com.example.semarv2.features.learning

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.semarv2.R
import com.example.semarv2.databinding.ActivityLearnDetailBinding
import com.example.semarv2.features.profile.SettingPreference
import com.example.semarv2.features.profile.SettingViewModel
import com.example.semarv2.features.videos.detail.VideoDetailActivity
import com.example.semarv2.util.Result
import com.example.semarv2.util.factory.SettingsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearnDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLearnDetailBinding
    private val viewModel : MateriViewModel by viewModels()
    private var link_download : String? = null
    private var file_name : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLearnDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackToList.setOnClickListener { backToList() }
        showMateriDetail()
        binding.btnDownloadMateri.setOnClickListener {
            downloadPdf(link_download!!, file_name!!)
        }
    }
    private fun backToList(){
        onBackPressedDispatcher.onBackPressed()
    }
    private fun showMateriDetail() {
        val materiId = intent.getStringExtra(LearnDetailActivity.MATERI_ID)
        viewModel.getMateriById(materiId.toString()).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbMateri.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val materiData = result.data
                    binding.pbMateri.visibility = View.GONE
                    binding.tvTitleMateri.text = materiData.title
                    // Dark mode preferences
                    binding.webView.loadDataWithBaseURL(null, materiData.materi_html, "text/html", "UTF-8", null)
                    link_download = materiData.materi_url
                    file_name = materiData.title

                }

                is Result.Error -> {
                    binding.pbMateri.visibility = View.GONE
                }
            }
        }
    }

    private fun downloadPdf(url: String, fileName : String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(fileName)
        request.setDescription("Downloading PDF file")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${fileName}.pdf"
        )
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    companion object{
        const val MATERI_ID = "materi_id"
    }
}