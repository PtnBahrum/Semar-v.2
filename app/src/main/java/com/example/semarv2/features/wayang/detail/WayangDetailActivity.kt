package com.example.semarv2.features.wayang.detail

import android.content.ContentValues.TAG
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.semarv2.R
import com.example.semarv2.databinding.ActivityWayangDetailBinding
import com.example.semarv2.features.wayang.SliderAdapter
import com.example.semarv2.features.wayang.WayangViewModel
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import com.example.semarv2.util.Result

@AndroidEntryPoint
class WayangDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWayangDetailBinding
    private val viewModel: WayangViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null

    // Change to var if it's initially null
    private var imageSlider: SliderView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWayangDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val Id = intent.getStringExtra(WAYANG_ID)
//        viewModel.getWayangByid(wayangName!!)
//        getDataWayang()
//        showImage()

        binding.btnView.setOnClickListener {
            if(binding.motionLayout.progress == 0f){
                binding.motionLayout.transitionToEnd()
            }else{
                binding.motionLayout.transitionToStart()
            }
        }
        binding.btnBackToList.setOnClickListener { backToList() }

        mediaPlayer = MediaPlayer.create(this, R.raw.gamelan_music)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        getWayangDataById()
    }


    private fun backToList(){
        onBackPressedDispatcher.onBackPressed()
    }
    private fun getWayangDataById(){
        val wayangId = intent.getStringExtra(WAYANG_ID)
        viewModel.getWayangById(wayangId!!).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbWayangDetail.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.pbWayangDetail.visibility = View.GONE
                    val wayangData = result.data
                    val viewPagerAdapter = SliderAdapter(wayangData.image)
                    binding.vpWayangImages.adapter = viewPagerAdapter
                    binding.indicatorWayangImages.setViewPager(binding.vpWayangImages)
                    binding.tvNameWayang.text = wayangData.name
                    binding.tvSifatWayang.text = wayangData.characteristic
                    binding.tvNameWayang2.text = wayangData.name
                    binding.tvParentWayang.text = wayangData.parent
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.tvBrosisWayang.text = Html.fromHtml(wayangData.sibling, Html.FROM_HTML_MODE_COMPACT)
                        binding.tvDescWayang.text = Html.fromHtml(wayangData.description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        @Suppress("DEPRECATION")
                        binding.tvDescWayang.text = Html.fromHtml(wayangData.description)
                        binding.tvBrosisWayang.text = Html.fromHtml(wayangData.sibling)
                    }
                }

                is Result.Error -> {
                    binding.pbWayangDetail.visibility = View.GONE
                    Log.d(TAG, result.toString())
                }
            }
        }
    }

//    private fun getDataWayang(){
//
//        lifecycle.coroutineScope.launchWhenCreated {
//            viewModel.wayangData.collect{
//                if (it.isLoading) {
//                    binding.pbWayangDetail.visibility = View.VISIBLE
//                }
//                if (it.error.isNotBlank()) {
//                    binding.pbWayangDetail.visibility = View.GONE
//                    this@WayangDetailActivity.displayToast(it.error)
//                }
//                it.data?.let { _wayangData ->
//                    binding.pbWayangDetail.visibility = View.GONE
//                    // Store the current video data in the ViewModel
//                    binding.tvNameWayang.text = _wayangData.name
//                    binding.tvSifatWayang.text = _wayangData.characteristic
//                    binding.tvNameWayang2.text = _wayangData.name
//                    binding.tvBrosisWayang.text = _wayangData.sibling.replace("\n", "<br>")
//                    binding.tvParentWayang.text = _wayangData.parent.replace("\n", "<br>")
//                    binding.tvDescWayang.text = _wayangData.description.replace("\n", "<br>")
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        binding.tvDescWayang.text = Html.fromHtml(_wayangData.description, Html.FROM_HTML_MODE_COMPACT)
//                    } else {
//                        @Suppress("DEPRECATION")
//                        binding.tvDescWayang.text = Html.fromHtml(_wayangData.description)
//                    }
//
////                    val sliderItemList = ArrayList<String>()
////                    for (image in _wayangData.image) {
////                        sliderItemList.add(image)
////                    }
////                    slideAdapter.renewItems(sliderItemList)
//
//                }
//            }
//        }
//    }

//    fun showImage(){
//        slideAdapter = SliderAdapter(this)
//        imageSlider = binding.imageSlider
//        imageSlider?.sliderAdapter.let {slideAdapter}
//        imageSlider!!.setIndicatorAnimation(IndicatorAnimationType.WORM)
//        imageSlider!!.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
//        imageSlider!!.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
//        imageSlider!!.setIndicatorSelectedColor(Color.WHITE)
//        imageSlider!!.setIndicatorUnselectedColor(Color.MAGENTA)
//    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        const val WAYANG_ID = "wayang_id"
    }
}