package com.example.weather.views.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.base.BaseViewModel
import com.example.weather.data.Location
import com.example.weather.data.NetworkState
import com.example.weather.data.source.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {
    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> get() = _locations

    fun getLocations() {
        viewModelScope.launch {
            repository.getLocations()
                .onStart { _networkState.value = NetworkState.Loading }
                .catch { _networkState.value = NetworkState.Error(it.message.toString()) }
                .collect { response ->
                    _locations.postValue(response)
                    _networkState.value = NetworkState.Success
                }
        }
    }
}