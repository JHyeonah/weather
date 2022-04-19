package com.example.weather.api

import com.example.weather.data.Location
import com.example.weather.data.Weather
import io.reactivex.rxjava3.core.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {
    @GET("search")
    suspend fun searchLocations(@Query("query") query: String): List<Location>

    @GET("{woeid}")
    suspend fun getWeather(@Path("woeid") id: Int): Weather

    @GET("search")
    fun searchLocationsRx(@Query("query") query: String): Flowable<List<Location>>

    @GET("{woeid}")
    fun getWeatherRx(@Path("woeid") id: Int): Flowable<Weather>

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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(RemoteService::class.java)
        }
    }
}