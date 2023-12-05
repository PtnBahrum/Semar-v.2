package com.example.semarv2.features.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(
    private val prefs: SettingPreference
    ) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean?> {
        return prefs.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            prefs.saveThemeSetting(isDarkModeActive)
        }
    }
}