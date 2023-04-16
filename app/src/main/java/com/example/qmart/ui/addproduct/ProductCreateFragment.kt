package com.example.qmart.ui.addproduct

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qmart.R
import com.example.qmart.addTextListener
import com.example.qmart.data.Repository
import com.example.qmart.databinding.FragmentProductCreateBinding
import com.example.qmart.ui.bottomsheet.CategoryBottomSheetFragment
import com.example.qmart.ui.bottomsheet.EMPTY


class ProductCreateFragment : Fragment() {
    private var selectedIndex = -1
    private var selectedCategory = EMPTY
    private var counter = 0

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
        fun newInstance() = ProductCreateFragment()
    }

    private lateinit var binding: FragmentProductCreateBinding
    private lateinit var viewModel: ProductCreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().viewModelStore.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ProductCreateViewModel::class.java)

        binding = FragmentProductCreateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setUI()
    }

    private fun setToolbar() {
        requireActivity().apply {
            setActionBar(binding.toolbar)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setUI() = with(binding) {
        viewModel.getProduct().apply {
            selectedCategory = this.category
            productNameEditText.setText(this.name)
            chooseCategoryTextView.text = if (this.category != EMPTY && this.category != "") selectedCategory else resources.getString(R.string.category)
            productDescriptionEditText.setText(this.description)

            if(productImage1 != Uri.parse("")) imageView1.setImageURI(this.productImage1)
            if(productImage2 != Uri.parse("")) imageView2.setImageURI(this.productImage2)
            if(productImage3 != Uri.parse("")) imageView3.setImageURI(this.productImage3)
        }

        chooseCategoryTextView.setOnClickListener {
            val fragment = CategoryBottomSheetFragment().apply {
                setCategorySelectedListener {
                    selectedIndex = it.first
                    selectedCategory = it.second
                    viewModel.setProductCategory(selectedCategory)
                    updateCategoryView()
                }
                setCategories(
                    selectedIndex,
                    Repository.categories
                )
            }
            fragment.show(parentFragmentManager, "Dialog")
        }

        productNameEditText.addTextListener {
            viewModel.setProductName(it)
        }
        productDescriptionEditText.addTextListener {
            viewModel.setProductDescription(it)
        }

        addPhotoButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PermissionChecker.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }

        }


        closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        continueButton.setOnClickListener {
            //viewModel.setProductFirstPage(Product(name = productNameEditText.text.toString(), category = selectedCategory, description = productDescriptionEditText.text.toString()))
            //openFragment(parentFragmentManager, ProductCreateNextFragment.newInstance(), "ProductCreateFragment")
            findNavController().navigate(R.id.action_fragmentProductCreate_to_productCreateNextFragment)
        }
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            ++counter
            when(counter){
                1 -> {
                    binding.imageView1.setImageURI(data?.data)
                    viewModel.setProductImage1(data?.data ?: Uri.parse(""))
                }
                2 -> {
                    binding.imageView2.setImageURI(data?.data)
                    viewModel.setProductImage2(data?.data ?: Uri.parse(""))
                }
                3 -> {
                    binding.imageView3.setImageURI(data?.data)
                    viewModel.setProductImage3(data?.data ?: Uri.parse(""))
                }
            }

            if(counter == 3){
                counter = 0
            }
        }
    }

    private fun updateCategoryView() {
        binding.chooseCategoryTextView.text = if (selectedCategory != EMPTY) selectedCategory else resources.getString(R.string.category)
    }
}