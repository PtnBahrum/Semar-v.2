package com.example.semarv2.features.kuis.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.semarv2.data.source.local.entity.Options
import com.example.semarv2.data.source.local.entity.Soal
import com.example.semarv2.databinding.ItemContentSoalBinding
import com.example.semarv2.features.profile.SettingViewModel

class ContentKuisAdapter(private val settingViewModel: SettingViewModel): RecyclerView.Adapter<ContentKuisAdapter.ViewHolder>() {
    private var soal = mutableListOf<Soal>()

    inner class ViewHolder(private val itemContentSoalBinding: ItemContentSoalBinding):
        RecyclerView.ViewHolder(itemContentSoalBinding.root) {
        fun bindItem(soal: Soal) {
            val optionsAdapter = OptionsAdapter(settingViewModel)
            itemContentSoalBinding.tvSoal.text = soal.body //memasukan quiz ke id tvquiz pada itemcontent.xml
            if (soal.image != null && soal.image.isNotEmpty()){
                itemContentSoalBinding.ivQuiz.visibility = View.VISIBLE //Jika ada SOAL GAMBAR AKAN DIMUNCULKAN TERLEBIH DAHULU
                Glide.with(itemView)
                    .load(soal.image)
                    .placeholder(android.R.color.darker_gray)
                    .into(itemContentSoalBinding.ivQuiz)
            }else{
                itemContentSoalBinding.ivQuiz.visibility = View.GONE
            }

            if (soal.options != null){
                optionsAdapter.setData(soal.options as MutableList<Options>)
                itemContentSoalBinding.rvOptionQuiz.adapter = optionsAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContentSoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(soal[position])//memecah data list menjadi satu objek
    }

    override fun getItemCount(): Int = soal.size

    fun setData(soal: MutableList<Soal>){
        this.soal = soal
        notifyDataSetChanged() // memberitahu keadapter jika ada data yang masuk dan data yang berubah
    }

    fun getResult(): MutableList<Soal> {
        return soal

    }
}