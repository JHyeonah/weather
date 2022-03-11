package com.example.weather.views.weather

import android.view.View
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.base.BaseFragment
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.views.adapters.WeatherAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModels()

    override fun initView() {
        viewModel.getWeathers()

        binding.layoutRefresh.setOnRefreshListener {
            viewModel.getWeathers()
        }
        binding.layoutRefresh.setOnChildScrollUpCallback { _, _ ->
            binding.recyclerWeather.canScrollVertically(-1)
        }
        val adapter = WeatherAdapter()
        binding.recyclerWeather.adapter = adapter

        subscribeUI(adapter)
    }

    private fun subscribeUI(adapter: WeatherAdapter) {
        viewModel.weathers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.recyclerWeather.post { binding.recyclerWeather.scrollToPosition(0) }
        }
    }

    override fun onLoading() {
        if (binding.layoutRefresh.isRefreshing) {
            binding.progressBar.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.layoutRefresh.isEnabled = false
        }
        binding.recyclerWeather.visibility = View.GONE
    }

    override fun onError() {
        binding.progressBar.visibility = View.GONE
        binding.layoutRefresh.isRefreshing = false
        binding.layoutRefresh.isEnabled = true
    }

    override fun onSuccess() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerWeather.visibility = View.VISIBLE
        binding.layoutRefresh.isRefreshing = false
        binding.layoutRefresh.isEnabled = true
    }
}