package com.kosiso.sfinventory.data.api

import com.kosiso.sfinventory.data.model.Product
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/products")
    suspend fun getProducts(): Response<List<Product>>

    @DELETE("/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}