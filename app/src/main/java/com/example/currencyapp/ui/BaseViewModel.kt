package com.example.currencyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(networkConnectivityObserver: NetworkConnectivityObserver) :
    ViewModel() {
    private val _networkStatus = MutableLiveData(ConnectivityObserver.Status.Unavailable)

    val networkStatus: LiveData<ConnectivityObserver.Status>
        get() = _networkStatus

    init {
        networkConnectivityObserver.observe().onEach {
            _networkStatus.value = it
        }.launchIn(viewModelScope)
    }
}