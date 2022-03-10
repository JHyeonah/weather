package com.example.weather.data

data class TotalWeather(
    val title: String?,
    val woeid: Int?,
    val weatherList: List<WeatherItem>?
)