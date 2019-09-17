package com.example.nasa.data.source.remote

import com.example.nasa.data.APod
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APodApiService {
    @GET("planetary/apod")
    suspend fun getAPods(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<List<APod>>

    suspend fun getApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Response<APod>
}