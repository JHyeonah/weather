package com.example.weather.data.source

import com.example.weather.api.ImageService
import com.example.weather.data.ImageResponse
import com.example.weather.data.Location
import com.example.weather.data.TotalWeather
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val source: RemoteDataSource,
    private val service: ImageService
    ) {
    fun getLocations(): Flow<List<Location>> = source.getLocations()

    fun getTotalWeathers(id: Int, title: String): Flow<TotalWeather> =
        source.getWeathers(id)
            .map { TotalWeather(title, id, it.consolidatedWeather) }

    fun getImages(query: String): Flowable<ImageResponse> = service.getSearchImage(query, 10)

    fun getVideos(query: String): Flowable<ImageResponse> = service.getSearchVideo(query, 10)
}
