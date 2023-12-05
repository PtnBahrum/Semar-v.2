package com.example.semarv2.features.kuis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.semarv2.R
import com.example.semarv2.databinding.ActivityResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultBinding
    private var scoreResult : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoreResult =  intent.getIntExtra(RESULT,1)
        binding.tvScore.text = scoreResult.toString()

        messageChange()
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,KuisListActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun messageChange(){
        if(scoreResult!! == 100){
            binding.message.text = getString(R.string.message_full_score)
            binding.tvStatusKuis.text = getString(R.string.message_graduation)
            binding.imageView.setImageResource(R.drawable.success_img)
        }else if(scoreResult!! in 70..90){
            binding.message.text = getString(R.string.message_high_score)
            binding.tvStatusKuis.text = getString(R.string.message_graduation)
            binding.imageView.setImageResource(R.drawable.success_img)
        }else{
            binding.message.text = getString(R.string.message_low_score)
            binding.tvStatusKuis.text = getString(R.string.message_fail)
            binding.imageView.setImageResource(R.drawable.fail_image)
        }
    }

    companion object{
        const val RESULT ="result"
    }
}