package com.example.qmart.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qmart.data.Product

class ProductViewModel : ViewModel() {
    //private val products: List<Product> = emptyList()

    private val productsMutableLiveData = MutableLiveData<List<Product>>()

    val productsLiveData: LiveData<List<Product>>
        get() = productsMutableLiveData

    fun setProducts(products: List<Product>){
        productsMutableLiveData.value = products
    }

    fun addProducts(products: List<Product>){
        val myProducts = mutableListOf<Product>()
        myProducts.addAll(productsMutableLiveData.value ?: emptyList())
        myProducts.addAll(products)
        productsMutableLiveData.value = myProducts
    }


    fun updateProductCost(product: Product){
        val myProducts = mutableListOf<Product>()
        myProducts.addAll(productsMutableLiveData.value ?: emptyList())
        myProducts.forEach {
            if(it.name == product.name){
                it.cost = product.cost
            }
        }
        productsMutableLiveData.value = myProducts
    }

    fun deleteProduct(product: Product) {
        val myProducts = mutableListOf<Product>()
        myProducts.addAll(productsMutableLiveData.value ?: emptyList())
        myProducts.remove(product)
        productsMutableLiveData.value = myProducts
    }



}