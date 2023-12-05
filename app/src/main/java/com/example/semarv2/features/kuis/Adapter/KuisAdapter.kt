package com.example.semarv2.features.kuis.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.semarv2.R
import com.example.semarv2.data.source.local.entity.KuisEntity
import com.example.semarv2.databinding.ItemQuizBinding
import com.example.semarv2.features.kuis.QuestionActivity


class KuisAdapter(
    private val listKuisItem: List<KuisEntity>,
) : RecyclerView.Adapter<KuisAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemQuizBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listKuisItem[position]
        val context = holder.itemView.context // Mengambil context dari itemView

        holder.apply {
            binding.tvQuizTitle.text = currentItem.title
            Glide.with(itemView.context)
                .load(currentItem.image)
                .into(binding.ivThumbnailKuis)



            if (currentItem.score > 0) {
                binding.layoutScore.visibility = View.VISIBLE
                binding.tvScore.text = currentItem.score.toString()

                // Menggunakan resources untuk mendapatkan string dan warna
                val buttonText = context.getString(R.string.replay)
                val buttonColor = ContextCompat.getColor(context, R.color.success_900)

                binding.btnStartQuiz.text = buttonText
                binding.btnStartQuiz.setBackgroundColor(buttonColor)

                when (currentItem.score) {
                    in 0..50 -> {
                        binding.containerScore.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.error_700
                            )
                        )
                    }

                    in 60..70 -> {
                        binding.containerScore.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.warning_700
                            )
                        )
                    }

                    in 80..90 -> {
                        binding.containerScore.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.success_700
                            )
                        )
                    }

                    else -> {
                        binding.containerScore.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.neutral_700
                            )
                        )
                    }
                }
            }

            binding.btnStartQuiz.setOnClickListener {
                val intent = Intent(context, QuestionActivity::class.java)
                intent.putExtra(QuestionActivity.QUIZ_ID, currentItem.id)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listKuisItem.size
}