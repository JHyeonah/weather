package com.example.weather.views.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.base.BaseViewModel
import com.example.weather.data.Location
import com.example.weather.data.NetworkState
import com.example.weather.data.TotalWeather
import com.example.weather.data.Weather
import com.example.weather.data.source.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {
    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> get() = _locations

    private val _weathers = MutableLiveData<List<TotalWeather>>()
    val weathers: LiveData<List<TotalWeather>> get() = _weathers

    @ExperimentalTime
    fun getLocations() {
        viewModelScope.launch {
            val mark = TimeSource.Monotonic.markNow()
            repository.getLocations()
                .onStart {
                    _networkState.value = NetworkState.Loading
                }
                .catch { _networkState.value = NetworkState.Error(it.message.toString()) }
                .map { list ->
                    list.map {async {
                        Log.d("DEBUG", "location : $it")
                        repository.getTotalWeathers(it.woeid ?: 0, it.title ?: "").first()
                    } }.awaitAll()
                }
                .collect { response ->
                    Log.d("DEBUG", "location : ${response.size}")
                    Log.d("DEBUG", "time : ${mark.elapsedNow()}")
                    _weathers.postValue(response)
                    _networkState.value = NetworkState.Success
                }
        }
    }

    @ExperimentalTime
    fun getLocationsRx() {
        val mark = TimeSource.Monotonic.markNow()
        _networkState.value = NetworkState.Loading
        repository.getLocationsRx()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io())
            .flatMap { list ->
                list.map { repository.getTotalWeathersRx(it.woeid ?: 0, it.title ?: "") }
            }
//            .map { list ->
//                list.map { repository.getTotalWeathersRx(it.woeid ?: 0, it.title ?: "").blockingFirst() }
//            }
            .subscribe({
                Log.d("DEBUG", "location : ${it.size}")
                Log.d("DEBUG", "rx end time : ${mark.elapsedNow()}")
                _weathers.postValue(it)
                _networkState.value = NetworkState.Success
            }, {
                _networkState.value = NetworkState.Error(it.message.toString())
            })
    }

    fun getTotalWeathersRx() {
    }
//
//    fun getWeathers(id: Int?, title: String?): Flow<TotalWeather> {
//        viewModelScope.launch {
//            repository.getTotalWeathers(id ?: 0, title ?: "")
//                .onStart { _networkState.value = NetworkState.Loading }
//                .catch { _networkState.value = NetworkState.Error(it.message.toString()) }
//                .collect { response ->
//                    _weathers.postValue(response)
//                    _networkState.value = NetworkState.Success
//                }
//        }
//    }
}