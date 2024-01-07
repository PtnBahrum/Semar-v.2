package com.example.semarv2.features.videos.detail

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import com.example.semarv2.util.Result
import com.example.semarv2.databinding.ActivityVideoDetailBinding
import com.example.semarv2.features.videos.VideoViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVideoDetailBinding
    private val viewModel: VideoViewModel by viewModels()
    private var videoIdYoutube: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        showVideoDetail()
        initializeVideoPlayer()

        binding.btnBackToHome.setOnClickListener { backToList() }
    }

    private fun backToList(){
        onBackPressedDispatcher.onBackPressed()
    }

    private fun showVideoDetail() {
        val videoId = intent.getStringExtra(VIDEO_ID)
        viewModel.getVideoById(videoId.toString()).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbVideoDetail.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val videoData = result.data
                    binding.pbVideoDetail.visibility = View.GONE
                    videoIdYoutube = videoData.video_url
                    binding.videoNameToolbar.text = videoData.title
                    binding.tvNameVideo.text = videoData.title
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.tvDesc.text = Html.fromHtml(videoData.description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        @Suppress("DEPRECATION")
                        binding.tvDesc.text = Html.fromHtml(videoData.description)
                    }
                }

                is Result.Error -> {
                    binding.pbVideoDetail.visibility = View.GONE
                }
            }
        }
//        lifecycle.coroutineScope.launchWhenCreated {
//            viewModel.videoData.collect{
//                if (it.isLoading) {
//                    binding.pbVideoDetail.visibility = View.VISIBLE
//                }
//                if (it.error.isNotBlank()) {
//                    binding.pbVideoDetail.visibility = View.GONE
//                    this@VideoDetailActivity.displayToast(it.error)
//                }
//                it.data?.collect {
//                    binding.pbVideoDetail.visibility = View.GONE
//                    // Store the current video data in the ViewModel
//                    binding.videoNameToolbar.text = it!!.title
//                    binding.tvNameVideo.text = it!!.title
//                    binding.tvDesc.text = it!!.description
//                    videoIdYoutube = it!!.video_url
//                }
//            }
//        }
    }

    private fun initializeVideoPlayer() {
        lifecycle.addObserver(binding.youtubePlayerView)

        val iFramePlayerOptions: IFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1)
            .build()

        binding.apply {
            youtubePlayerView.addFullscreenListener(object : FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    youtubePlayerView.visibility = View.GONE
                    fullScreenViewContainer.visibility = View.VISIBLE
                    fullScreenViewContainer.addView(fullscreenView)
                    setSystemBarVisibility(false)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }

                @SuppressLint("SourceLockedOrientationActivity")
                override fun onExitFullscreen() {
                    youtubePlayerView.visibility = View.VISIBLE
                    fullScreenViewContainer.visibility = View.GONE
                    fullScreenViewContainer.removeAllViews()
                    setSystemBarVisibility(true)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                }
            })

            youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoIdYoutube ?: "dQw4w9WgXcQ", 0f)
                }
            }, iFramePlayerOptions)
        }
    }

    private fun setSystemBarVisibility(isVisible: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isVisible) {
                window.decorView.windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
            } else {
                window.decorView.windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
                window.decorView.windowInsetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            if (isVisible) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            } else {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }

    companion object {
        const val VIDEO_ID = "video_id"
    }
}