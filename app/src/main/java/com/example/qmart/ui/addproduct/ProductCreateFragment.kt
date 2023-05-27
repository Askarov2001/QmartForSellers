package com.example.qmart.ui.addproduct

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.qmart.addTextListener
import com.example.qmart.data.Categories
import com.example.qmart.data.Product
import com.example.qmart.databinding.FragmentProductCreateBinding
import com.example.qmart.ui.bottomsheet.CategoryBottomSheetFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class ProductCreateFragment : Fragment() {
    private var selectedIndex = -1
    private var selectedCategory: Categories = Categories.PRODUCTS
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
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val storageRef: StorageReference by lazy {
        storage.reference
    }

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
            chooseCategoryTextView.text = getString(selectedCategory.nameRes)
            productDescriptionEditText.setText(this.description)
//
//            if (productImage1 != Uri.parse("")) imageView1.setImageURI(this.productImage1)
//            if (productImage2 != Uri.parse("")) imageView2.setImageURI(this.productImage2)
//            if (productImage3 != Uri.parse("")) imageView3.setImageURI(this.productImage3)
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
                    Categories.values().toList()
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
        productPrice.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        productPrice.addTextListener {
            viewModel.setProductCost(it.toIntOrNull() ?: 0)
        }

        addPhotoButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ==
                    PermissionChecker.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }

        }


        closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        continueButton.setOnClickListener {
            if (productNameEditText.text.toString()
                    .isNotEmpty() && productDescriptionEditText.text.toString().isNotEmpty()
                && selectedCategory != Categories.EMPTY
            ) {
                writeNewProductToDb(viewModel.getProduct())
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "ЗАПОЛНИТЕ ВСЕ ПОЛЯ ПЛИЗ!", Toast.LENGTH_SHORT)
                    .show()
            }
            //viewModel.setProductFirstPage(Product(name = productNameEditText.text.toString(), category = selectedCategory, description = productDescriptionEditText.text.toString()))
            //openFragment(parentFragmentManager, ProductCreateNextFragment.newInstance(), "ProductCreateFragment")
        }
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // File or Blob
            data?.data?.let {
                val file = Uri.fromFile(File(it.path.toString()))
                val uploadTask = storageRef.putFile(file)
                uploadTask.addOnSuccessListener {
                    Log.d("WWWWWW", it.task.result.storage.downloadUrl.toString())
                }.addOnCompleteListener {
                    Log.d("WWWWWW", "COMPLETE")

                }.addOnFailureListener {
                    Log.d("WWWWWW", it.message.toString())

                }
                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    Log.d("DOWNLOAD", storageRef.downloadUrl.toString())
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("DOWNLOAD", downloadUri.toString())
                    } else {
                        Log.d("WWWWWW", "FAIL")
                        // Handle failures
                        // ...
                    }
                }
            }
        }
    }

    private fun updateCategoryView() {
        binding.chooseCategoryTextView.text = getString(selectedCategory.nameRes)
    }

    private fun writeNewProductToDb(product: Product) {
        val addedCategories = ArrayList<String>()
        database.child("CATEGORIES").get().addOnSuccessListener {
            it.children.forEach {
                addedCategories.add(it.key.toString())
            }
        }
        if (!addedCategories.contains(product.category.toString().uppercase())) {
            database.child("CATEGORIES").child(product.category.toString().uppercase())
                .setValue(product.category.toString().uppercase())
        }
        database.child(product.category.toString().uppercase()).child(product.id).setValue(product)
    }
}