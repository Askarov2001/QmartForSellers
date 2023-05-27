package com.example.qmart.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.qmart.R
import com.example.qmart.databinding.FragemntResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment(R.layout.fragemnt_reset_password) {
    private val binding: FragemntResetPasswordBinding by viewBinding()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            resetPassword.setOnClickListener {
                val sPassword = resetEmail.text.toString()
                auth.sendPasswordResetEmail(sPassword)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Проверьте почту!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                    }

            }
        }


    }
}