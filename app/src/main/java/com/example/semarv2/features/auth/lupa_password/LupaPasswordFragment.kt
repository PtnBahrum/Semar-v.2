package com.example.semarv2.features.auth.lupa_password

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.semarv2.MainActivity
import com.example.semarv2.R
import com.example.semarv2.databinding.FragmentLupaPasswordBinding
import com.example.semarv2.databinding.ItemDialogCardBinding
import com.example.semarv2.databinding.ItemResetPasswordSuccessBinding
import com.example.semarv2.features.auth.AuthViewModel
import com.example.semarv2.features.auth.login.LoginActivity
import com.example.semarv2.util.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LupaPasswordFragment : DialogFragment() {
    private lateinit var binding: FragmentLupaPasswordBinding
    private val viewModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLupaPasswordBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { dialog?.dismiss() }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated  {
            viewModel.resetPasswordResult.collect{
                if (it.isLoading) {
                    setLoadingVisibility(true)
                }
                if (it.error.isNotBlank()) {
                    setLoadingVisibility(false)
                    showToast(it.error)
                }
                it.data?.let {
                    setLoadingVisibility(false)
                    showSuccessDialog(getString(R.string.forgotPasswordMessage))
                }
            }
        }
        binding.btnSendReset.setOnClickListener {
            resetPassword()
        }
    }

    override fun onResume() {
        super.onResume()

        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setLoadingVisibility(isVisible: Boolean) {
        binding.apply {
            pbForgetPassword.visibility = if (isVisible) View.VISIBLE else View.GONE
            btnSendReset.isEnabled = !isVisible
            btnClose.isEnabled = !isVisible
            tvEmail.isEnabled = !isVisible
        }
    }
    private fun showSuccessDialog(message: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = ItemResetPasswordSuccessBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvMessage.text = message
        dialogBinding.btnCloseDialog.setOnClickListener {
            dialog?.dismiss()
        }
        dialog.show()
    }

    private fun resetPassword() {
        val email = binding.tvEmail.text.toString()

        if (email.isEmpty()){
            showToast(getString(R.string.error_email_null))
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showToast(getString(R.string.error_email_invalid))
        }else{
            viewModel.resetPassword(email, requireContext())
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}