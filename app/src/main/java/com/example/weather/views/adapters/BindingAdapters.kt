package com.example.weather.views.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.weather.extensions.humidityFormat
import com.example.weather.extensions.tempFormat

@BindingAdapter("floatToTemp")
fun bindFloatToTemp(view: TextView, num: Float) {
    view.text = num.tempFormat()
}

@BindingAdapter("intToHumidity")
fun bindIntToHumidity(view: TextView, num: Int) {
    view.text = num.humidityFormat()
}

@BindingAdapter("abbrToImage")
fun bindAbbrToImage(view: ImageView, abbr: String) {
    val url = "https://www.metaweather.com/static/img/weather/png/64/$abbr.png"
    Glide.with(view.context).load(url).into(view)
}
