package com.kosiso.sfinventory.data.repository

import com.kosiso.sfinventory.data.api.ApiService
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.database.ProductDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepoImpl @Inject constructor(
    val productDao: ProductDao,
    val apiService: ApiService): MainRepo {

        // add product to local db
    override suspend fun addProductIntoLocaldb(product: Product) {
        productDao.insertProduct(product)
    }

    override suspend fun addProductToServer(product: Product): Result<Unit> {
        return try {
            apiService.addProduct(product)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

        // get list of product
    override suspend fun getProductListFromLocaldb(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    //get singular product with id
    override suspend fun getProductFromLocaldb(id: String): Product {
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

    override suspend fun deleteProductFromServer(id: String): Result<Unit> {
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

    override suspend fun deleteProductFromLocaldb(id: String) {
        return productDao.deleteProductById(id)
    }

    override suspend fun updateProductInLocaldb(product: Product) {
        return productDao.updateProduct(product)
    }

    override suspend fun updateProductOnServer(product: Product): Result<Unit> {
        return try {
            apiService.updateProduct(product.id, product)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}