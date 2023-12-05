package com.example.semarv2.features.learning

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.semarv2.data.source.local.entity.MateriEntity
import com.example.semarv2.databinding.ItemMateriBinding
import com.example.semarv2.features.videos.detail.VideoDetailActivity

class MateriAdapter(private val listMateriItem: List<MateriEntity>,
) : RecyclerView.Adapter<MateriAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemMateriBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriAdapter.ViewHolder {
        val binding = ItemMateriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MateriAdapter.ViewHolder, position: Int) {
        val currentItem = listMateriItem[position]
        holder.apply {
            binding.tvTitleMateri.text = currentItem.title
            binding.tvDesc.text = currentItem.description
            Glide.with(itemView.context)
                .load(currentItem.image)
                .into(binding.ivMateriThumbnail)

            itemView.setOnClickListener {
                val intent = Intent(it.context, LearnDetailActivity::class.java)
                intent.putExtra(LearnDetailActivity.MATERI_ID, currentItem.id)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listMateriItem.size

}