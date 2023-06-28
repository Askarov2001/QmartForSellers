package com.example.qmart.ui.result

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.example.qmart.R
import com.example.qmart.data.CategoryArgument
import com.example.qmart.databinding.FragmentResultBinding
import com.example.qmart.ui.BaseFragment
import com.example.qmart.ui.bottomsheet.CategoryBottomSheetFragment

class ResultFragment : BaseFragment(R.layout.fragment_result) {
    private lateinit var binding: FragmentResultBinding
    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val result = intent.getBooleanExtra("result", false)
        val result = arguments?.getBoolean(ARGUMENTS) ?: false

        setToolbar()

        with(binding) {


            if (result) {
                resultTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                resultTextView.text = resources.getString(R.string.request_ok)
                nextStepTextView.isVisible = true
            } else {
                resultTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                resultTextView.text = resources.getString(R.string.request_fail)
            }

            continueButton.setOnClickListener {

            }
        }

    }

    private fun setToolbar(){
        baseActivity?.apply {
            setActionBar(binding.toolbar)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    companion object {
        internal const val ARGUMENTS = "arguments"

        fun newInstance(resultArgument: Boolean) =
            ResultFragment().apply {
                arguments = bundleOf(Pair(ResultFragment.ARGUMENTS, resultArgument))
            }
    }

}



