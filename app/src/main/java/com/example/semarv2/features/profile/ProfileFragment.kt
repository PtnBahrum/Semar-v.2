package com.example.semarv2.features.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.semarv2.R
import com.example.semarv2.databinding.FragmentProfileBinding
import com.example.semarv2.databinding.ItemCardLogoutBinding
import com.example.semarv2.features.auth.login.LoginActivity
import com.example.semarv2.features.profile.editPassword.EditPasswordActivity
import com.example.semarv2.features.profile.editProfile.EditProfileActivity
import com.example.semarv2.util.factory.SettingsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var settingViewModel : SettingViewModel

    private val settingsName = "settings"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(settingsName)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserData()
        bindDataUser()
        initViewModel()
        observeViewModel()

        binding.settingEditProfile.setOnClickListener { intentToActivity(EditProfileActivity::class.java) }
        binding.settingEditPassword.setOnClickListener { intentToActivity(EditPasswordActivity::class.java) }
        binding.btnLogout.setOnClickListener {
            val message: String = resources.getString(R.string.logout_message)
            showLogOutDialog(message)
        }
        binding.settingDarkMode.setOnClickListener {
            val isChecked = binding.switchDarkMode.isChecked
            binding.switchDarkMode.isChecked = !isChecked
            settingViewModel.saveThemeSetting(!isChecked)
        }
        binding.settingLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        checkLoginUserMethod()
    }

    private fun intentToActivity(targetActivityClass: Class<*>){
        val intent = Intent(requireContext(), targetActivityClass)
        startActivity(intent)
    }

    private fun bindDataUser(){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.userData.collect {
                if (it.isLoading) {
//                    binding.pbProfile.visibility = View.VISIBLE
                }
                if (it.error.isNotBlank()) {
//                    binding.pbProfile.visibility = View.GONE
                    showToast(it.error)
                }
                it.data?.let { _user ->
//                    binding.pbProfile.visibility = View.GONE
                    binding.tvName.text = _user.name
                    binding.tvEmail.text = _user.email
                    Glide
                        .with(this@ProfileFragment)
                        .load(_user.image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(binding.ivUserImage)
                }
            }
        }
    }

    private fun showToast(message : String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun showLogOutDialog(message: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = ItemCardLogoutBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvMessage.text = message

        dialogBinding.btnYes.setOnClickListener {
            viewModel.logOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkLoginUserMethod(){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.isLoggedInWithGoogle.collect { isLoggedIn ->
                // Update UI based on the login status
                if (isLoggedIn) {
                    binding.dividerEdPassword.visibility = View.GONE
                    binding.settingEditPassword.visibility = View.GONE
                } else {
                    // User logged out or logged in with other method
                    binding.dividerEdPassword.visibility = View.VISIBLE
                    binding.settingEditPassword.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initViewModel() {
        val preferences = SettingPreference.getInstance(requireActivity().dataStore)
        settingViewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(preferences)
        )[SettingViewModel::class.java]
    }

    private fun observeViewModel() {
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean? ->
            if (isDarkModeActive == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkMode.isChecked = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserData()
        bindDataUser()
    }

}