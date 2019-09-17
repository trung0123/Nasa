package com.example.nasa.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nasa.data.APod
import java.util.*

@Dao
interface APodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApod(vararg aPod: APod)

    @Query("SELECT * FROM apods WHERE date = :date ORDER BY date DESC LIMIT 1")
    suspend fun getApod(date: Date): APod?

    @Query("SELECT * FROM apods WHERE media_type == 'image' ORDER BY date DESC")
    fun getApods(): DataSource.Factory<Int, APod>

    @Query("SELECT date FROM apods ORDER BY date DESC LIMIT 1")
    suspend fun getLatestAPodDate(): Date?
}