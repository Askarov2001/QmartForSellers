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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.qmart.BuildConfig
import com.example.qmart.MainActivity
import com.example.qmart.R
import com.example.qmart.SharedPref
import com.example.qmart.addTextListener
import com.example.qmart.data.Categories
import com.example.qmart.data.Product
import com.example.qmart.databinding.FragmentProductCreateBinding
import com.example.qmart.ui.BaseFragment
import com.example.qmart.ui.bottomsheet.CategoryBottomSheetFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class ProductCreateFragment : BaseFragment(R.layout.fragment_product_create) {
    private var selectedIndex = -1
    private var selectedCategory: Categories = Categories.PRODUCTS

    companion object {
        fun newInstance() = ProductCreateFragment()
    }

    private lateinit var binding: FragmentProductCreateBinding
    private lateinit var viewModel: ProductCreateViewModel
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val storageRef: StorageReference by lazy {
        storage.reference.child("product/${viewModel.getProduct().id}")
    }

    private val permission: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity?.viewModelStore?.clear()
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
            }
        }
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                this.uri = uri
                Glide.with(requireContext())
                    .load(uri)
                    .into(binding.imageView1)

            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(baseActivity!!).get(ProductCreateViewModel::class.java)

        binding = FragmentProductCreateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setUI()
    }

    private fun setToolbar() {
        baseActivity?.apply {
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
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                shouldShowRequestPermissionRationale(
                    permission
                ) -> {
                }

                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        permission
                    )
                }
            }

        }


        closeButton.setOnClickListener {
            baseActivity!!.onBackPressedDispatcher.onBackPressed()
        }

        continueButton.setOnClickListener {
            if (productNameEditText.text.toString()
                    .isNotEmpty() && productDescriptionEditText.text.toString().isNotEmpty()
            ) {
                uploadImage()
            } else {
                Toast.makeText(requireContext(), "ЗАПОЛНИТЕ ВСЕ ПОЛЯ ПЛИЗ!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateCategoryView() {
        binding.chooseCategoryTextView.text = getString(selectedCategory.nameRes)
    }

    private fun writeNewProductToDb(product: Product) {
        val prod: Product = product.apply {

                sellerId = baseActivity?.getValue(SharedPref.UID)
                merchant = baseActivity?.getValue(SharedPref.MERCHANT)
        }
        val addedCategories = ArrayList<String>()
        database.child("CATEGORIES").get().addOnSuccessListener {
            it.children.forEach {
                addedCategories.add(it.key.toString())
            }
        }
        if (!addedCategories.contains(prod.category.toString().uppercase())) {
            database.child("CATEGORIES").child(prod.category.toString().uppercase())
                .setValue(prod.category.toString().uppercase())
        }
        database.child(prod.category.toString().uppercase()).child(prod.id)
            .setValue(prod)
        baseActivity?.onBackPressedDispatcher?.onBackPressed()
    }

    private fun uploadImage() {
        this.uri?.let {
            binding.loader.visibility = View.VISIBLE
            storageRef.putFile(it).addOnCompleteListener {
                binding.loader.visibility = View.GONE
                storageRef.downloadUrl.addOnSuccessListener {
                    viewModel.setProductImage(it.toString())
                    writeNewProductToDb(viewModel.getProduct())
                }
            }
        } ?: run {
            writeNewProductToDb(viewModel.getProduct())
        }
    }
}