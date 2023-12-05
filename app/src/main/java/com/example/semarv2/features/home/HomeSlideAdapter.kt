package com.example.semarv2.features.home

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.semarv2.R
import com.example.semarv2.data.source.remote.model.BannerSlideData
import com.example.semarv2.features.kuis.KuisListActivity
import com.example.semarv2.features.videos.VideoListActivity
import com.example.semarv2.features.wayang.WayangListActivity
import com.squareup.picasso.Picasso

class HomeSlideAdapter(
    private val context: Context,
    private val bannerList: List<BannerSlideData>,
    private val viewPager: ViewPager
) : PagerAdapter() {

    private val TAG = "SliderAdapter"
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var currentPosition = 0

    private val handler = Handler()
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (currentPosition == bannerList.size - 1) {
                currentPosition = 0
            } else {
                currentPosition++
            }
            viewPager.currentItem = currentPosition
            handler.postDelayed(this, 5000) // Change position every 4 seconds
        }
    }

    init {
        // Start auto-sliding when the adapter is created
        handler.postDelayed(updateRunnable, 5000)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount() = bannerList.size

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.item_banner, view, false)!!
        val imageView = imageLayout.findViewById<ImageView>(R.id.iv_banner_image)
        val bannerData = bannerList[position]
        Picasso.get().load(bannerData.img)
            .placeholder(R.drawable.bg_card_view)
            .error(R.drawable.bg_card_view)
            .into(imageView)
        imageLayout.setOnClickListener {
            val intent = when (bannerData.type) {
                "kuis" -> Intent(context, KuisListActivity::class.java)
                "wayang" -> Intent(context, WayangListActivity::class.java)
                "video" -> Intent(context, VideoListActivity::class.java)
                else -> null
            }
            intent?.putExtra("itemId", bannerData.id)
            intent?.let {
                context.startActivity(it)
            }
        }
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

    // Stop auto-sliding when the adapter is destroyed
    fun stopAutoSlide() {
        handler.removeCallbacks(updateRunnable)
    }
}