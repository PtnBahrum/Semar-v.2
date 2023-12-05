package com.example.semarv2.features.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.semarv2.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}