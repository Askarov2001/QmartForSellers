package com.example.qmart.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.qmart.MainActivity
import com.example.qmart.R
import com.example.qmart.data.Sellers
import com.example.qmart.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val binding: FragmentSignInBinding by viewBinding()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            logNow.setOnClickListener {
                (requireActivity() as LoginActivity).addFragment(LogFragment())
            }
            back.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            signupBtn.setOnClickListener {
                val name = signName.text.toString()
                val phone = signPhone.text.toString()
                val email = signEmail.text.toString()
                val password = signPassword.text.toString()
                val business = signBusiness.text.toString()

                signPasswordLayout.isPasswordVisibilityToggleEnabled = true

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || business.isEmpty()) {
                    if (name.isEmpty()) {
                        signName.error = "Введите Имя"
                    }
                    if (phone.isEmpty()) {
                        signPhone.error = "Введите Телефон"
                    }
                    if (email.isEmpty()) {
                        signEmail.error = "Введите Электронную Почту"
                    }
                    if (password.isEmpty()) {
                        signPassword.error = "Введите Пароль"
                        signPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    }
                    if (business.isEmpty()) {
                        signBusiness.error = "Введите название вашего бизнеса"
                    }
                    Toast.makeText(requireContext(), "Введите данные", Toast.LENGTH_SHORT).show()

                } else if (!email.matches(emailPattern.toRegex())) {
                    signEmail.error = "Введите почту правильно"
                    Toast.makeText(requireContext(), "Введите почту правильно", Toast.LENGTH_SHORT)
                        .show()
                } else if (password.length < 6) {
                    signPassword.error = "Должно быть более 6 символов"
                    Toast.makeText(
                        requireContext(),
                        "Должно быть более 6 символов",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (phone.length < 11) {
                    signPhone.error = "Введите правильный номер телефона"
                    Toast.makeText(
                        requireContext(),
                        "Введите правильный номер телефона",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val databaseRef =
                                database.reference.child("sellers").child(auth.currentUser!!.uid)
                            val sellers =
                                Sellers(
                                    name,
                                    phone,
                                    email,
                                    password,
                                    business,
                                    auth.currentUser!!.uid
                                )

                            databaseRef.setValue(sellers).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val Intent = Intent(activity, MainActivity::class.java)
                                    startActivity(Intent)

                                    Toast.makeText(
                                        requireContext(),
                                        "Аккаунт успешно создан!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Ошибка! Повторите еще раз!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
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