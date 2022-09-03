package com.example.currencyapp.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

open class BaseViewModel(context: Context): ViewModel() {
    private val _networkStatus = MutableLiveData(ConnectivityObserver.Status.Unavailable)

    val networkStatus: LiveData<ConnectivityObserver.Status>
        get() = _networkStatus

    init {
        NetworkConnectivityObserver(context).observe().onEach {
            _networkStatus.value = it
        }.launchIn(viewModelScope)
    }
}