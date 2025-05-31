package com.kosiso.sfinventory.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Timestamp
import kotlin.let

class Converters {
    // Convert Timestamp to Long (for storing in local database)
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it) }
    }

    // Convert Long (timestamp) to Timestamp
    @TypeConverter
    fun toTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }
}
