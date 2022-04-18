package com.example.weather.api

import com.example.weather.BuildConfig
import com.example.weather.data.ImageResponse
import io.reactivex.rxjava3.core.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {
    @GET("search/image")
    fun getSearchImage(@Query("query") query: String, @Query("size") size: Int): Flowable<ImageResponse>

    @GET("search/vclip")
    fun getSearchVideo(@Query("query") query: String, @Query("size") size: Int): Flowable<ImageResponse>


    companion object {
        private const val BASE_URL = "https://dapi.kakao.com/v2/"

        fun create(): ImageService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_ACCESS_KEY}")
                            .build()
                        chain.proceed(request)
                    }
                    .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ImageService::class.java)
        }
    }
}