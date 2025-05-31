package com.kosiso.sfinventory.data.repository

import com.kosiso.sfinventory.data.api.ApiService
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.database.ProductDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepoImpl @Inject constructor(
    val productDao: ProductDao,
    val apiService: ApiService): MainRepo {


        // get list of product
    override suspend fun getProductsFromLocaldb(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    //get singular product with id
    override suspend fun getProductFromLocaldb(id: Int): Product {
        return productDao.getProductById(id)
    }

    override suspend fun insertProductsListIntoLocaldb(products: List<Product>): Result<Unit> {
        return try {
            productDao.insertProductList(products)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductsFromServer(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("API error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProductFromServer(id: Int): Result<Unit> {
        return try {
            val deleteProduct = apiService.deleteProduct(id)
            if (deleteProduct.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("API error: ${deleteProduct.code()} - ${deleteProduct.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProductFromLocaldb(id: Int) {
        return productDao.deleteProductById(id)
    }

}