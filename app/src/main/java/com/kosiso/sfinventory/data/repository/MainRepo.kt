package com.kosiso.sfinventory.data.repository

import com.kosiso.sfinventory.data.model.Product
import kotlinx.coroutines.flow.Flow

interface MainRepo {

    suspend fun getProductsFromLocaldb(): Flow<List<Product>>
    suspend fun insertProductsListIntoLocaldb(products: List<Product>): Result<Unit>
    suspend fun getProductFromLocaldb(id: Int): Product

//    suspend fun insertProductIntoLocaldb(product: Product)
//    suspend fun updateProductIntoLocaldb(product: Product)
    suspend fun deleteProductFromLocaldb(id: Int)

    suspend fun getProductsFromServer(): Result<List<Product>>
    suspend fun deleteProductFromServer(id: Int): Result<Unit>
}