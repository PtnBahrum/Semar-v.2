package com.example.semarv2.features.auth.register

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
import com.example.semarv2.data.source.remote.model.User
import com.example.semarv2.databinding.ActivityRegisterBinding
import com.example.semarv2.databinding.ItemDialogCardBinding
import com.example.semarv2.features.auth.AuthViewModel
import com.example.semarv2.features.auth.login.LoginActivity
import com.example.semarv2.util.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.user.collect {
                if (it.isLoading) {
                    binding.btnRegister.isEnabled = false
                    binding.btnBackInLogin.isEnabled = false
                    binding.tvToLogin.isEnabled = false
                    binding.pbRegister.visibility = View.VISIBLE
                }
                if (it.error.isNotBlank()) {
                    binding.btnRegister.isEnabled = true
                    binding.btnBackInLogin.isEnabled = true
                    binding.tvToLogin.isEnabled = true
                    binding.pbRegister.visibility = View.GONE
                    this@RegisterActivity.displayToast(it.error)
                }
                it.data?.let {
                    binding.btnRegister.isEnabled = true
                    binding.btnBackInLogin.isEnabled = true
                    binding.tvToLogin.isEnabled = true
                    binding.pbRegister.visibility = View.GONE
                    showLoginDialog(getString(R.string.registerMessage))
                }
            }
        }

        binding.btnBackInLogin.setOnClickListener{ backToLogin()}
        binding.btnRegister.setOnClickListener{ register() }
        binding.tvToLogin.setOnClickListener {
            backToLogin()
        }
    }

    private fun showLoginDialog(message: String) {
        val dialog = Dialog(this)
        val dialogBinding = ItemDialogCardBinding.inflate(LayoutInflater.from(this))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvMessage.text = message
        dialogBinding.btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        dialog.show()
    }

    private fun backToLogin() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun register() {
        val name = binding.tvName.text.toString()
        val email = binding.tvEmail.text.toString()
        val password = binding.tvPassword.text.toString()
        val confpassword = binding.tvPasswordConfirmation.text.toString()
        val image = "https://firebasestorage.googleapis.com/v0/b/semar-v2.appspot.com/o/default_profile.png?alt=media&token=2f8a1c6f-d869-4a6a-b533-ef63b28d87a2"

        val user = User(
            name = name,
            email = email,
            image = image,
            score_quiz_1 = 0,
            score_quiz_2 = 0,
            score_quiz_3 = 0,
            score_quiz_4 = 0,
        )

        if (name.isEmpty()){
            this@RegisterActivity.displayToast(getString(R.string.error_name_null))
        }else if (email.isEmpty()){
            this@RegisterActivity.displayToast(getString(R.string.error_email_null))
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this@RegisterActivity.displayToast(getString(R.string.error_email_invalid))
        }else if (password.isEmpty()){
            this@RegisterActivity.displayToast(getString(R.string.error_password_null))
        }else if (password.length < 8){
            this@RegisterActivity.displayToast(getString(R.string.error_password_invalid))
        }else if (confpassword.isEmpty()){
            this@RegisterActivity.displayToast(getString(R.string.error_konfirmasi_password_null))
        }else if(confpassword != password){
            this@RegisterActivity.displayToast(getString(R.string.error_konfirmasi_password_invalid))
        }else{
            viewModel.register(email, password, user)
        }
    }
}