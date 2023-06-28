package com.example.qmart.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.qmart.MainActivity
import com.example.qmart.R
import com.example.qmart.databinding.FragmentLogBinding
import com.google.firebase.auth.FirebaseAuth

class LogFragment : Fragment(R.layout.fragment_log) {
    private val binding: FragmentLogBinding by viewBinding()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            signNow.setOnClickListener {
                (activity as LoginActivity).addFragment(SignInFragment())
            }
            back.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            forgetPass.setOnClickListener {
                (activity as LoginActivity).addFragment(ResetPasswordFragment())
            }

            loginbtn.setOnClickListener {
                logPasswordLayout.isPasswordVisibilityToggleEnabled = true

                val email = logEmail.text.toString()
                val password = logPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    if (email.isEmpty()) {
                        logEmail.error = "Введите электронную почту"
                    }
                    if (password.isEmpty()) {
                        logPassword.error = "Введите пароль"
                        logPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    }
                    Toast.makeText(requireContext(), "Введите информацию", Toast.LENGTH_SHORT)
                        .show()
                } else if (!email.matches(emailPattern.toRegex())) {
                    logEmail.error = "Введите почту правильно"
                    Toast.makeText(requireContext(), "Введите почту правильно", Toast.LENGTH_SHORT)
                        .show()
                } else if (password.length < 6) {
                    logPassword.error = "Должно быть более 6 символов"
                    Toast.makeText(
                        requireContext(),
                        "Должно быть более 6 символов",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Ошибка! Повторите еще раз!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }
        }
    }
}