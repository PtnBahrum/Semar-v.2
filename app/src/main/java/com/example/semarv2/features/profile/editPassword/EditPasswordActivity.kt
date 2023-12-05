package com.example.semarv2.features.profile.editPassword

import android.animation.Animator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.example.semarv2.MainActivity
import com.example.semarv2.R
import com.example.semarv2.databinding.ActivityEditPasswordBinding
import com.example.semarv2.databinding.ItemDialogSuccessBinding
import com.example.semarv2.features.profile.ProfileFragment
import com.example.semarv2.features.profile.ProfileViewModel
import com.example.semarv2.util.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditPasswordBinding
    private val viewModel : ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToProfil.setOnClickListener { backToProfil() }
        editPasswordState()

        binding.btnSave.setOnClickListener {
            editPassword()
        }
    }

    private fun backToProfil(){
        onBackPressedDispatcher.onBackPressed()
    }
    private fun editPasswordState(){
        lifecycle.coroutineScope.launchWhenCreated{
            viewModel.updatePasswordResult.collect{ result ->
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

    private fun editPassword(){
        val oldPass = binding.edOldPassword.text.toString()
        val newPass = binding.edNewPassword.text.toString()
        val confrPass = binding.edConfirmPassword.text.toString()

        if (oldPass.isEmpty()){
            this@EditPasswordActivity.displayToast(getString(R.string.oldPasswordEmpty))
        }else if (newPass.isEmpty()){
            this@EditPasswordActivity.displayToast(getString(R.string.newPasswordEmpty))
        }else if (newPass.length < 8){
            this@EditPasswordActivity.displayToast(getString(R.string.error_password_invalid))
        }else if (confrPass.isEmpty()){
            this@EditPasswordActivity.displayToast(getString(R.string.error_konfirmasi_password_null))
        }else if(confrPass != newPass){
            this@EditPasswordActivity.displayToast(getString(R.string.error_konfirmasi_password_invalid))
        }else{
            viewModel.changePassword(oldPass, newPass)
        }
    }
    private fun setLoadingVisibility(isVisible: Boolean) {
        binding.apply {
            pbEditPassword.visibility = if (isVisible) View.VISIBLE else View.GONE
            edOldPassword.isEnabled = !isVisible
            edNewPassword.isEnabled = !isVisible
            edConfirmPassword.isEnabled = !isVisible
            btnSave.isEnabled = !isVisible
            btnBackToProfil.isEnabled = !isVisible
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
            val intent = Intent(this@EditPasswordActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }, 2000) // Set the delay duration (in milliseconds)
    }
}