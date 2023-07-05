package com.ihp.frontoffice.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object{

        val TIMEOUT_SECONDS = 15L
        fun getApiService(url: String): InventoryClient {
            val loggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()
            val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(InventoryClient::class.java)
        }
    }
}