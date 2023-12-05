package com.example.semarv2.features.videos

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.semarv2.data.source.local.entity.VideoEntity
import com.example.semarv2.databinding.ItemVideoBinding
import com.example.semarv2.features.videos.detail.VideoDetailActivity


class VideoAdapter(
    private val listVideoItem: List<VideoEntity>,
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoAdapter.ViewHolder, position: Int) {
        val currentItem = listVideoItem[position]
        holder.apply {
            binding.tvNameVideo.text = currentItem.title
            binding.tvDurationVideo.text = currentItem.video_duration
            Glide.with(itemView.context)
                .load(currentItem.image_thumbnail)
                .into(binding.ivVideo)

            itemView.setOnClickListener {
                val intent = Intent(it.context, VideoDetailActivity::class.java)
                intent.putExtra(VideoDetailActivity.VIDEO_ID, currentItem.id)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listVideoItem.size

//    // Fungsi ini digunakan untuk mengupdate data pada adapter
//    fun updateData(newList: List<VideoEntity>) {
//        listVideoItem = newList
//        notifyDataSetChanged()
//    }
}