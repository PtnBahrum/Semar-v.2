package com.example.semarv2.features.kuis.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.RecyclerView
import com.example.semarv2.R
import com.example.semarv2.data.source.local.entity.Options
import com.example.semarv2.databinding.ItemOptionBinding
import com.example.semarv2.features.profile.SettingViewModel

class OptionsAdapter(private val viewModel: SettingViewModel) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>()  {

    private var options = mutableListOf<Options>()
    inner class ViewHolder (private val itemOptionsBinding: ItemOptionBinding):RecyclerView.ViewHolder(itemOptionsBinding.root) {

        fun bindItem(option: Options) {
            itemOptionsBinding.tvOpsi.text = option.option
            itemOptionsBinding.tvQuizOptionValue.text = option.answer

            if (option.is_click!!){
                activeCheckAnswer()
            }else {
                inActiveCheckAnwer()
            }

            itemView.setOnClickListener{

                for (i in 0 until options.size){
                    options[i].is_click = i == adapterPosition
                }
                notifyDataSetChanged()
            }
        }

        // Dark mode preferences
        private val isDarkModeActive = viewModel.getThemeSettings().value ?: false

        private fun inActiveCheckAnwer() {
            if (isDarkModeActive){
                itemOptionsBinding.opsiContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.brown_700))
                itemOptionsBinding.tvOpsi.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                itemOptionsBinding.tvQuizOptionValue.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }else{
                itemOptionsBinding.opsiContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.orange_50))
                itemOptionsBinding.tvOpsi.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_900))
                itemOptionsBinding.tvQuizOptionValue.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_900))
            }
        }

        private fun activeCheckAnswer() {
            if (isDarkModeActive){
                itemOptionsBinding.opsiContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.orange_50))
                itemOptionsBinding.tvOpsi.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_900))
                itemOptionsBinding.tvQuizOptionValue.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_900))
            }else{
                itemOptionsBinding.opsiContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.brown_700))
                itemOptionsBinding.tvOpsi.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                itemOptionsBinding.tvQuizOptionValue.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(options[position])
    }

    override fun getItemCount(): Int = options.size

    fun setData(options: MutableList<Options>){
        this.options = options
        notifyDataSetChanged()
    }

}