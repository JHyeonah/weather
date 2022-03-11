package com.example.weather.views.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.base.BaseViewModel
import com.example.weather.data.NetworkState
import com.example.weather.data.TotalWeather
import com.example.weather.data.source.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {
    private val _weathers = MutableLiveData<List<TotalWeather>>()
    val weathers: LiveData<List<TotalWeather>> get() = _weathers

    fun getWeathers() {
        viewModelScope.launch {
            repository.getLocations()
                .onStart { _networkState.value = NetworkState.Loading }
                .map { list ->
                    list.map {
                        async {
                            repository.getTotalWeathers(it.woeid ?: 0, it.title ?: "").first()
                        }
                    }.awaitAll()
                }
                .catch { _networkState.value = NetworkState.Error(it.message.toString()) }
                .collect { response ->
                    _weathers.postValue(response)
                    _networkState.value = NetworkState.Success
                }
        }
    }
}