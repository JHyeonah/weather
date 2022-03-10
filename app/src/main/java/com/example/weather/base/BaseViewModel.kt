package com.example.weather.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.NetworkState

open class BaseViewModel : ViewModel() {
    protected val _networkState = MutableLiveData<NetworkState>(NetworkState.Success)
    val networkState: LiveData<NetworkState>
        get() = _networkState
}