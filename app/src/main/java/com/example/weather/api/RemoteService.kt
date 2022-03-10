package com.example.weather.api

import com.example.weather.data.Location
import com.example.weather.data.Weather
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {
    @GET("search")
    suspend fun searchLocations(@Query("query") query: String): List<Location>

    @GET("{woeid}")
    suspend fun getWeather(@Path("woeid") id: Int): Weather

    companion object {
        private const val BASE_URL = "https://www.metaweather.com/api/location/"

        fun create(): RemoteService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RemoteService::class.java)
        }
    }
}