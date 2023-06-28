package com.example.qmart.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qmart.R
import com.example.qmart.databinding.FragmentSettingBinding
import com.example.qmart.ui.BaseFragment

class SettingFragment : BaseFragment(R.layout.fragment_setting) {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
    }

    fun setUI() = with(binding) {
        cityDelivery.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }
        closeButton.setOnClickListener {
            baseActivity!!.onBackPressedDispatcher.onBackPressed()
        }
    }

}