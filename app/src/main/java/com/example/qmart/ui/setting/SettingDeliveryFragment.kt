package com.example.qmart.ui.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qmart.R
import com.example.qmart.databinding.FragmentSettingBinding
import com.example.qmart.databinding.FragmentSettingDeliveryBinding

class SettingDeliveryFragment : Fragment() {

    companion object {
        fun newInstance() = SettingDeliveryFragment()
    }

    private lateinit var binding: FragmentSettingDeliveryBinding
    private lateinit var viewModel: SettingDeliveryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewModel = ViewModelProvider(this).get(SettingDeliveryViewModel::class.java)
        binding = FragmentSettingDeliveryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() = with(binding) {

        closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

}