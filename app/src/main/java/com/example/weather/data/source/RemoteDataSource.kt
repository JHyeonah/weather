package com.example.weather.data.source

import com.example.weather.api.RemoteService
import com.example.weather.data.Location
import com.example.weather.data.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: RemoteService) {
    fun getLocations(): Flow<List<Location>> = flow {
        val response = service.searchLocations("se")
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getWeathers(id: Int): Flow<Weather> = flow {
        val response = service.getWeather(id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getLocationsRx() = service.searchLocationsRx("se")

    fun getWeathersRx(id: Int) = service.getWeatherRx(id)
}