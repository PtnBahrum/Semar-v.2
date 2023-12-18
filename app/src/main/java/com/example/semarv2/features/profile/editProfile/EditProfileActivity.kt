package com.example.semarv2.features.profile.editProfile

import android.animation.Animator
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import com.example.semarv2.MainActivity
import com.example.semarv2.R
import com.example.semarv2.databinding.ActivityEditProfileBinding
import com.example.semarv2.databinding.ItemDialogSuccessBinding
import com.example.semarv2.features.profile.ProfileFragment
import com.example.semarv2.features.profile.ProfileViewModel
import com.example.semarv2.util.displayToast
import com.example.semarv2.util.reduceFileImg
import com.example.semarv2.util.uriToFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfileBinding
    private val viewModel : ProfileViewModel by viewModels()
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserData()
        bindDataUser()
        binding.btnChangeUserImage.setOnClickListener { startIntentGallery() }

        editProfil()
        binding.btnSave.setOnClickListener {
            val name = binding.edName.text.toString()
            if(name.length > 20){
                this@EditProfileActivity.displayToast(getString(R.string.name_much_long))

            }else{
                viewModel.updateProfil(name,imageUri)
            }
        }
        binding.btnBackToProfil.setOnClickListener { backToProfil() }

    }

    private fun backToProfil(){
        onBackPressedDispatcher.onBackPressed()
    }

    private fun startIntentGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.input_choose_from_gallery))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {

            val selectedImg = result.data?.data as Uri
            var file = uriToFile(selectedImg, this)
            file = reduceFileImg(file, 500000)
            imageUri = selectedImg
            val result = BitmapFactory.decodeFile(file.path)
            binding.ivUserImage.setImageBitmap(result)
        }
    }

    private fun bindDataUser() {
        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.userData.collect {
                if (it.isLoading) {
                    binding.pbEditProfile.visibility = View.VISIBLE
                }
                if (it.error.isNotBlank()) {
                    binding.pbEditProfile.visibility = View.GONE
                    displayToast(it.error)
                }
                it.data?.let { _user ->
                    binding.pbEditProfile.visibility = View.GONE
                    binding.edName.setText(_user.name)
                    binding.edEmail.setText(_user.email)
                    binding.apply {
                        edEmail.isEnabled = false
                        edEmail.alpha = 0.25f
                    }
                    Glide
                        .with(this@EditProfileActivity)
                        .load(_user.image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(binding.ivUserImage)
                }
            }
        }
    }
    private fun editProfil(){
        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.updateProfilResult.collect { result ->
                setLoadingVisibility(result.isLoading)

                if (result.error.isNotBlank()) {
                    displayToast(result.error)
                } else {
                    // Update the user data in the ProfileFragment
                    result.data?.let {
                        showDialog()
                    }
                }
            }
        }
    }

    private fun setLoadingVisibility(isVisible: Boolean) {
        binding.apply {
            pbEditProfile.visibility = if (isVisible) View.VISIBLE else View.GONE
            edName.isEnabled = !isVisible
            edEmail.isEnabled = !isVisible
            btnSave.isEnabled = !isVisible
            btnBackToProfil.isEnabled = !isVisible
            btnChangeUserImage.isEnabled = !isVisible
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        val dialogBinding = ItemDialogSuccessBinding.inflate(LayoutInflater.from(this))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogBinding.root)

        // Add a listener to LottieAnimationView's animation completion event
        dialogBinding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationEnd(p0: Animator) {
                dialog.dismiss()
            }
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })

        dialog.show() // Display the dialog

        // Delay calling onBackPressed until the dialog is visible
        dialog.window?.decorView?.postDelayed({
            val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }, 2000) // Set the delay duration (in milliseconds)
    }
}