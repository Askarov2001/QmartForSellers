package com.example.qmart.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.qmart.R
import com.example.qmart.databinding.FragmentWelocomeBinding

class WelcomeFragment : Fragment(R.layout.fragment_welocome) {
    private val binding: FragmentWelocomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goBtn.setOnClickListener {
            (activity as LoginActivity).addFragment(LogFragment())
        }

        binding.startBtn.setOnClickListener {
            (requireActivity() as LoginActivity).addFragment(PhoneVerFragment())
        }
    }
}