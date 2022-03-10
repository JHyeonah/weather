package com.example.weather.data.source

import com.example.weather.data.Location
import com.example.weather.data.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataRepository @Inject constructor(private val source: RemoteDataSource) {
    fun getLocations(): Flow<List<Location>> = source.getLocations()

    fun getWeathers(id: Int): Flow<Weather> = source.getWeathers(id)
}