package com.example.semarv2.features.wayang

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.semarv2.data.source.local.entity.WayangEntity
import com.example.semarv2.databinding.ItemWayangBinding
import com.example.semarv2.features.wayang.detail.WayangDetailActivity


class WayangAdapter(
    private val listWayangItem: List<WayangEntity>
) : BaseAdapter() {

    class ViewHolder(var binding: ItemWayangBinding)

    override fun getCount(): Int {
        return listWayangItem.size
    }

    override fun getItem(position: Int): Any {
        return listWayangItem[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemWayangBinding
        val viewHolder: ViewHolder

        if (convertView == null) {
            binding = ItemWayangBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            val holder = ViewHolder(binding)
            binding.root.tag = holder
            viewHolder = holder
        } else {
            viewHolder = convertView.tag as ViewHolder
            binding = viewHolder.binding
        }

        val currentItem = listWayangItem[position]

        viewHolder.apply {
            binding.tvWayangName.text = currentItem.name
            Glide.with(binding.root.context)
                .load(currentItem.image_thumbnail)
                .into(binding.ivWayangKarakter)

            binding.root.setOnClickListener {
                val intent = Intent(it.context, WayangDetailActivity::class.java)
                intent.putExtra(WayangDetailActivity.WAYANG_ID, currentItem.id)
                it.context.startActivity(intent)
            }
        }

        return binding.root
    }
}