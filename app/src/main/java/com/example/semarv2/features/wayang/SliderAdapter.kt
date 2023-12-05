package com.example.semarv2.features.wayang

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.semarv2.databinding.ItemImageBinding


class SliderAdapter(
    private val listImage: List<String>
) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderAdapter.ViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderAdapter.ViewHolder, position: Int) {
        holder.apply {
            Glide.with(itemView.context)
                .load(listImage[position])
                .into(binding.ivWayang)
        }
    }

    override fun getItemCount(): Int = listImage.size
}