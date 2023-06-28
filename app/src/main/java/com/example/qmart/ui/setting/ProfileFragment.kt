package com.example.qmart.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.qmart.MainActivity
import com.example.qmart.R
import com.example.qmart.SharedPref
import com.example.qmart.addTextListener
import com.example.qmart.databinding.FragmentProfileBinding
import com.example.qmart.ui.BaseFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val database: DatabaseReference by lazy {
        Firebase.database.reference.child("sellers")
    }
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() = with(binding) {
        database.child(baseActivity?.getValue(SharedPref.UID).toString())
            .child("business").get().addOnSuccessListener {
                companyEd.setText(it.value.toString())
            }
        database.child(baseActivity?.getValue(SharedPref.UID).toString())
            .child("name").get().addOnSuccessListener {
                nameEditText.setText(it.value.toString())
            }
        database.child(baseActivity?.getValue(SharedPref.UID).toString())
            .child("phone").get().addOnSuccessListener {
                phoneEd.setText(it.value.toString())
            }
        companyEd.addTextChangedListener {
            database.child(baseActivity?.getValue(SharedPref.UID).toString())
                .child("business").setValue(it.toString())
        }
        nameEditText.addTextChangedListener {
            database.child(baseActivity?.getValue(SharedPref.UID).toString())
                .child("name").setValue(it.toString())
        }
        phoneEd.addTextChangedListener {
            database.child(baseActivity?.getValue(SharedPref.UID).toString())
                .child("phone").setValue(it.toString())
        }
    }

}