package com.example.semarv2.features.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.example.semarv2.MainActivity
import com.example.semarv2.R
import com.example.semarv2.data.source.remote.model.User
import com.example.semarv2.databinding.ActivityLoginBinding
import com.example.semarv2.features.auth.AuthViewModel
import com.example.semarv2.features.auth.lupa_password.LupaPasswordFragment
import com.example.semarv2.features.auth.register.RegisterActivity
import com.example.semarv2.util.displayToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.user.collect {
                if (it.isLoading) {
                    binding.btnLogin.isEnabled = false
                    binding.btnLoginGoogle.isEnabled = false
                    binding.tvToRegister.isEnabled = false
                    binding.pbAuth.visibility = View.VISIBLE
                }
                if (it.error.isNotBlank()) {
                    binding.btnLogin.isEnabled = true
                    binding.btnLoginGoogle.isEnabled = true
                    binding.tvToRegister.isEnabled = true
                    binding.pbAuth.visibility = View.GONE
                    this@LoginActivity.displayToast(it.error)
                }
                it.data?.let {
                    binding.btnLogin.isEnabled = true
                    binding.btnLoginGoogle.isEnabled = true
                    binding.tvToRegister.isEnabled = true
                    binding.pbAuth.visibility = View.GONE
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
        }

        binding.tvToRegister.setOnClickListener { clickToRegister() }
        binding.tvForgetPassword.setOnClickListener {
            LupaPasswordFragment().show(supportFragmentManager, TAG_FORGET_PASSWORD)
        }
        binding.btnLogin.setOnClickListener{ login()}

        // Initialize Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnLoginGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun clickToRegister() {
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun login() {
        val email = binding.tvEmail.text.toString()
        val password = binding.tvPassword.text.toString()

        if (email.isEmpty()){
            this@LoginActivity.displayToast(getString(R.string.error_email_null))
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this@LoginActivity.displayToast(getString(R.string.error_email_invalid))
        }else if (password.isEmpty()){
            this@LoginActivity.displayToast(getString(R.string.error_password_null))
        }else if (password.length < 8){
            this@LoginActivity.displayToast(getString(R.string.error_password_invalid))
        }else{
            viewModel.login(email, password)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Sign in with Google and add user data
                val account = task.getResult(ApiException::class.java)
                viewModel.signInWithGoogleAndAddUser(data!!, getUserFromGoogleAccount(account!!))
            } catch (e: ApiException) {
                // Handle Google sign-in failure
                displayToast("Google Sign-In failed")
            }
        }
    }

    private fun getUserFromGoogleAccount(account: GoogleSignInAccount): User {
        // Extract necessary user data from GoogleSignInAccount
        val user = User()
        user.email = account.email!!
        user.name = account.displayName!!
        user.image = account.photoUrl.toString()
        user.id = account.id!!
        user.score_quiz_1 = 0
        user.score_quiz_2 = 0
        user.score_quiz_3 = 0
        user.score_quiz_4 = 0
        // Add other necessary user data

        return user
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "AuthActivity"
        private const val TAG_FORGET_PASSWORD = "ForgetPasswordFragment"
    }
}