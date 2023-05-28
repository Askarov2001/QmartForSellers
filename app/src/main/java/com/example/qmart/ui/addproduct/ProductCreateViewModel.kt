package com.example.qmart.ui.addproduct

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.qmart.data.Categories
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

    fun setProductCategory(it: Categories) {
        product.category = it
    }

    fun setProductDescription(it: String) {
        product.description = it
    }

    fun setProductCost(it: Int) {
        product.cost = it
    }

    fun setProductImage(image: String) {
        product.images = image
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