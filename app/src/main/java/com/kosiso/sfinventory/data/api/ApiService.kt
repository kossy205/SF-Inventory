package com.kosiso.sfinventory.data.api

import com.kosiso.sfinventory.data.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("/products")
    suspend fun addProduct(@Body product: Product): Response<Unit>

    @GET("/products")
    suspend fun getProducts(): Response<List<Product>>

    @PUT("/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Body product: Product
    ): Response<Unit>

    @DELETE("/products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<Unit>
}