package com.example.weather.views.weather

import android.util.Log
import androidx.constraintlayout.motion.widget.Debug
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.FloatingWindow
import com.example.weather.base.BaseViewModel
import com.example.weather.data.Document
import com.example.weather.data.NetworkState
import com.example.weather.data.TotalWeather
import com.example.weather.data.source.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {
    private val _weathers = MutableLiveData<List<TotalWeather>>()
    val weathers: LiveData<List<TotalWeather>> get() = _weathers

    private val _images = MutableLiveData<List<Document>>()
    val images: LiveData<List<Document>> get() = _images

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

    fun getWeathersRx() {
        _networkState.value = NetworkState.Loading
        val disposable = repository.getLocationsRx()
            .flatMapIterable {
                it
            }
            .flatMap {
                repository.getTotalWeathersRx(it.woeid ?: 0, it.title ?: "")
            }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                _weathers.postValue(it)
                _networkState.value = NetworkState.Success
            }, {
                _networkState.value = NetworkState.Error(it.message.toString())
            })
    }

    fun getImages(query: String) {
        _networkState.value = NetworkState.Loading
        val imageFlowable = repository.getImages(query).flatMap { item ->
            Log.d("DEBUG", "get image flowable : $item")
            Flowable.fromIterable(item.documents)
        }
        val videoFlowable = repository.getVideos(query).flatMap { item ->
            Log.d("DEBUG", "get video flowable : $item")
            Flowable.fromIterable(item.documents)
        }

        val disposable = Flowable.zip(imageFlowable, videoFlowable, { a, b ->
            Log.d("DEBUG", "get zip image : $a")
            Log.d("DEBUG", "get zip video : $b")
            Document(b.title, null, null, null, null, a.image_url)
        })
            .subscribeOn(Schedulers.io())
            .toList()
//            .toSortedList { i1, i2 -> i2.datetime!!.compareTo(i1.datetime!!) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("DEBUG", "get : $it")
                _images.value = it
                _networkState.value = NetworkState.Success
            }, {
                Log.d("DEBUG", "get images err : $it")
                _networkState.value = NetworkState.Error(it.localizedMessage ?: "")
            })

    }

}