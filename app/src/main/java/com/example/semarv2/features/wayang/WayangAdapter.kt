package com.example.semarv2.features.wayang

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.semarv2.data.source.remote.model.Wayang
import com.example.semarv2.data.source.local.entity.WayangEntity
import com.example.semarv2.databinding.ItemWayangBinding
import com.example.semarv2.features.wayang.detail.WayangDetailActivity
import com.example.semarv2.util.Resource
import kotlinx.coroutines.flow.Flow


class WayangAdapter(
    private val listWayangItem: List<WayangEntity>
    ) : RecyclerView.Adapter<WayangAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemWayangBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WayangAdapter.ViewHolder {
        val binding = ItemWayangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WayangAdapter.ViewHolder, position: Int) {
        val currentItem = listWayangItem[position]
        holder.apply {
            binding.tvWayangName.text = currentItem.name
            Glide.with(itemView.context)
                .load(currentItem.image_thumbnail)
                .into(binding.ivWayangKarakter)

            itemView.setOnClickListener {
                val intent = Intent(it.context, WayangDetailActivity::class.java)
                intent.putExtra(WayangDetailActivity.WAYANG_ID, currentItem.id)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listWayangItem.size
}

//class WayangAdapter(
//    private val onItemClick: (WayangEntity) -> Unit
//) : RecyclerView.Adapter<WayangAdapter.ViewHolder>() {
//
//    private var listWayangItem: List<WayangEntity> = emptyList()
//
//    class ViewHolder(var binding: ItemWayangBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WayangAdapter.ViewHolder {
//        val binding = ItemWayangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: WayangAdapter.ViewHolder, position: Int) {
//        val currentItem = listWayangItem[position]
//        holder.apply {
//            binding.tvWayangName.text = currentItem.name
//            Glide.with(itemView.context)
//                .load(currentItem.image_thumbnail)
//                .into(binding.ivWayangKarakter)
//
//            itemView.setOnClickListener {
//                onItemClick(currentItem)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = listWayangItem.size
//
//    // Fungsi ini digunakan untuk mengupdate data pada adapter
//    fun updateData(newList: List<WayangEntity>) {
//        listWayangItem = newList
//        notifyDataSetChanged()
//    }
//}