package com.kosiso.sfinventory.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.data.repository.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(val mainRepo: MainRepo): ViewModel() {
    private val _productsList = MutableStateFlow<Result<List<Product>>>(Result.success(emptyList()))
    val productsList: StateFlow<Result<List<Product>>> = _productsList

    private val _product = MutableStateFlow<Result<Product>>(Result.success(Product()))
    val product: StateFlow<Result<Product>> = _product

    init {
        getProductsFromServer()
    }

    // get product list from local db
    fun getProductsFromLocaldb(){
        viewModelScope.launch {
            mainRepo.getProductsFromLocaldb().collect{
                _productsList.value = Result.success(it)
                Log.i("get Products From Localdb", "success: $it")
            }
        }
    }

    // get singular product from local db
    fun getProductFromLocaldb(id: Int){
        viewModelScope.launch {
            val product = mainRepo.getProductFromLocaldb(id)
            _product.value = Result.success(product)
        }
    }

    fun getProductsFromServer(){
        viewModelScope.launch {
            val result = mainRepo.getProductsFromServer()
            result.apply {
                onSuccess {productList->
                    insertProductListIntoLocaldb(productList)
                    Log.i("get Products From Server", "success: $productList")
                }
                onFailure {
                    Log.i("get Products From Server", "error" + it.message.toString())
                }
            }
        }
    }

    fun insertProductListIntoLocaldb(products: List<Product>){
        viewModelScope.launch {
            mainRepo.insertProductsListIntoLocaldb(products).apply {
                onSuccess {
                    getProductsFromLocaldb()
                    Log.i("insert Products Into Localdb", "success")
                }
                onFailure {
                    Log.i("insert Products Into Localdb", "error" + it.message.toString())
                }
            }
        }
    }

    fun deleteProduct(id: Int){
        viewModelScope.launch {
            mainRepo.deleteProductFromLocaldb(id)
            mainRepo.deleteProductFromServer(id)
        }
    }

}