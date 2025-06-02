package com.kosiso.sfinventory.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kosiso.sfinventory.data.Converters
import com.kosiso.sfinventory.data.model.Product
import com.kosiso.sfinventory.database.ProductDao

@Database(
    entities = [Product::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class RoomDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
}