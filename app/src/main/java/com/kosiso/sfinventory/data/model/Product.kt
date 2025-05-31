package com.kosiso.sfinventory.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "products_table")
data class Product(
    @PrimaryKey()
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val quantity: Int = 0,
    val price: Long = 0L,
    val supplier: String = "",
    val category: String = "",
    val lastUpdated: Timestamp = Timestamp(System.currentTimeMillis())
)

