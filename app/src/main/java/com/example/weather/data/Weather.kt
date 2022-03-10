package com.example.weather.data

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("consolidated_weather") val consolidatedWeather: List<WeatherItem>?
)