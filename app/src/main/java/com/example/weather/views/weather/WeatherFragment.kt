package com.example.weather.views.weather

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.base.BaseFragment
import com.example.weather.databinding.FragmentWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModels()

    override fun initView() {
        viewModel.getLocations()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.locations.observe(viewLifecycleOwner) {
            Log.d("DEBUG", "$it")
        }
    }
}