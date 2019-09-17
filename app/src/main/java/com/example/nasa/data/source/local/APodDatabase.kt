package com.example.nasa.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nasa.data.APod
import com.example.nasa.utils.DateTypeConverter

@Database(entities = [APod::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class APodDatabase : RoomDatabase() {
    abstract val apodDao: APodDao
}