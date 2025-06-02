package com.kosiso.sfinventory.data.repository

import com.kosiso.sfinventory.data.model.Product
import kotlinx.coroutines.flow.Flow

interface MainRepo {

    suspend fun addProductIntoLocaldb(product: Product)
    suspend fun insertProductsListIntoLocaldb(products: List<Product>): Result<Unit>
    suspend fun updateProductInLocaldb(product: Product)
    suspend fun getProductListFromLocaldb(): Flow<List<Product>>
    suspend fun getProductFromLocaldb(id: String): Product
    suspend fun deleteProductFromLocaldb(id: String)

    suspend fun addProductToServer(product: Product): Result<Unit>
    suspend fun getProductsFromServer(): Result<List<Product>>
    suspend fun deleteProductFromServer(id: String): Result<Unit>
    suspend fun updateProductOnServer(product: Product): Result<Unit>

}