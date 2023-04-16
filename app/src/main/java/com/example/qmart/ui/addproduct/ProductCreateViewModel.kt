package com.example.qmart.ui.addproduct

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.qmart.data.Product

class ProductCreateViewModel : ViewModel() {

    private val product = Product()

    /*private val productMutableLiveData = MutableLiveData<Product>()

    val productLiveData: LiveData<Product>
        get() = productMutableLiveData

    init {
        productMutableLiveData.postValue(Product())
    }
     */

    fun getProduct(): Product {
        return product
    }

    fun setProductName(it: String) {
        product.name = it
    }

    fun setProductCategory(it: String) {
        product.category = it
    }

    fun setProductDescription(it: String) {
        product.description = it
    }

    fun setProductCountry(it: String) {
        product.country = it
    }

    fun setProductExpirationDate(it: String) {
        product.expirationDate = it
    }

    fun setProductPackageType(it: String) {
        product.packageType = it
    }

    fun setProductType(it: String) {
        product.type = it
    }

    fun setProductIngredients(it: String) {
        product.ingredients = it
    }

    fun setProductCharacteristics(it: String) {
        product.characteristics = it
    }

    fun setProductImage1(it: Uri) {
        product.productImage1 = it
    }

    fun setProductImage2(it: Uri) {
        product.productImage2 = it
    }

    fun setProductImage3(it: Uri) {
        product.productImage3 = it
    }

    /*
    fun setProductFirstPage(product: Product){
        productMutableLiveData.value = productMutableLiveData.value?.copy(
            name = product.name,
            category = product.category,
            description = product.description
        )
    }

    fun setProductSecondPage(product: Product){
        productMutableLiveData.value = productMutableLiveData.value?.copy(
            country = product.country,
            expirationDate = product.expirationDate,
            packageType = product.packageType,
            type = product.type,
            ingredients = product.ingredients,
            characteristics = product.characteristics
        )
    }

    fun setProductV(product: Product) {
        productMutableLiveData.value = productMutableLiveData.value?.copy(
            name = product.name,
            category = product.category,
            description = product.description,
            country = product.country,
            expirationDate = product.expirationDate,
            packageType = product.packageType,
            type = product.type,
            ingredients = product.ingredients,
            characteristics = product.characteristics
        )
    }
     */
}