package com.example.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherItem(
    val id: Long?,
    @SerializedName("weather_state_name") val weatherStateName: String?,
    @SerializedName("weather_state_abbr") val weatherStateAbbr: String?,
    @SerializedName("the_temp") val theTemp: Float?,
    val humidity: Int?
)