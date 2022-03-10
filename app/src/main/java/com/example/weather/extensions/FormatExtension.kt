package com.example.weather.extensions

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun Float.tempFormat(): String {
    val format = NumberFormat.getNumberInstance(Locale.ENGLISH) as DecimalFormat
    format.applyPattern("##")
    return format.format(this).toString() + "℃"
}

fun Int.humidityFormat(): String {
    return "$this%"
}