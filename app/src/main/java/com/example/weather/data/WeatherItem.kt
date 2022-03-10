package com.example.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherItem(
    val id: Long?,
    @SerializedName("weather_state_name") val stateName: String?,
    @SerializedName("weather_state_abbr") val stateAbbr: String?,
    @SerializedName("the_temp") val temp: Float?,
    val humidity: Int?
)