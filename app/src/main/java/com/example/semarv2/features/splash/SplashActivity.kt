package com.example.semarv2.features.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.semarv2.MainActivity
import com.example.semarv2.R
import com.example.semarv2.databinding.ActivitySplashBinding
import com.example.semarv2.features.auth.AuthViewModel
import com.example.semarv2.features.onboarding.OnboardingActivity
import com.example.semarv2.features.profile.SettingPreference
import com.example.semarv2.features.profile.SettingViewModel
import com.example.semarv2.util.factory.SettingsViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val authViewModel: AuthViewModel by viewModels()

    private val settingsName = "settings"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(settingsName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dark mode preferences
        val pref = SettingPreference.getInstance(dataStore)
        val viewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(pref)
        )[SettingViewModel::class.java]
        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean? ->
            when (isDarkModeActive) {
                null -> {
                    // Check if user has OS dark mode activated
                    if (this.resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
                    ) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        viewModel.saveThemeSetting(true)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        viewModel.saveThemeSetting(false)
                    }
                }

                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        // Authentication checking
        authViewModel.loggedUser()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        lifecycle.coroutineScope.launchWhenCreated {
            authViewModel.user.collect {
                if (it.isLoading) {
                }
                if (it.error.isNotBlank()) {
                    Handler().postDelayed({
                        startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                        finish()
                    }, 600)
                    playAnimation()
                }
                it.data?.let {
                    Handler().postDelayed({
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }, 600)
                    playAnimation()
                }
            }
        }
    }

    private fun playAnimation() {
        val appName = ObjectAnimator.ofFloat(binding.appName, View.ALPHA, 1f).setDuration(500)
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(logo, appName)
            start()
        }
    }
}