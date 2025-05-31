package com.kosiso.sfinventory.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kosiso.sfinventory.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductList(list: List<Product>)

    @Query("SELECT * FROM products_table WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product

    @Query("SELECT * FROM products_table ORDER BY id DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("DELETE FROM products_table WHERE id = :productId")
    suspend fun deleteProductById(productId: Int)
}