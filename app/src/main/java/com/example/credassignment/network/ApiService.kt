package com.example.credassignment.network
import com.example.credassignment.models.ApiResponse
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("test_mint")
    fun getItems(): Call<ApiResponse>

    object RetrofitInstance {
        private val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
            .build()

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.mocklets.com/p6764/") // Base URL
                .client(client) // Set the custom client
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}