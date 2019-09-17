package com.example.nasa.di.app

import android.content.Context
import androidx.room.Room
import com.example.nasa.data.source.local.APodDatabase
import com.example.nasa.data.source.remote.APodApiService
import com.example.nasa.utils.GsonDateAdapter
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object DataModule {

    @JvmStatic
    @Singleton
    @Provides
    fun providesAPodDatabase(context: Context): APodDatabase = Room.databaseBuilder(
        context,
        APodDatabase::class.java,
        "apod.db"
    ).build()

    @JvmStatic
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        // Creating log interceptors with basic level
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC

        // OkHttp Client
        // Adding log interceptors and setting timeouts
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()

        // Creating Gson instance with GsonDateAdapter type adapter
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, GsonDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesAPodApiService(retrofit: Retrofit):APodApiService = retrofit.create()
}