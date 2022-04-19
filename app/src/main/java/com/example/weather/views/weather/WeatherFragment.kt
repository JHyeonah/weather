package com.example.weather.views.weather

import android.util.Log
import android.view.View
import com.example.weather.R
import com.example.weather.base.BaseFragment
import com.example.weather.databinding.FragmentWeatherBinding
import androidx.fragment.app.viewModels
import com.example.weather.views.adapters.WeatherAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModels()

    @ExperimentalTime
    override fun initView() {
        viewModel.getLocations()

//        viewModel.getLocationsRx()
        val adapter = WeatherAdapter()
        binding.recyclerWeather.adapter = adapter

        subscribeUI(adapter)
    }

    private fun subscribeUI(adapter: WeatherAdapter) {
        viewModel.weathers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerWeather.visibility = View.GONE
    }

    override fun onError() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onSuccess() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerWeather.visibility = View.VISIBLE
    }
}