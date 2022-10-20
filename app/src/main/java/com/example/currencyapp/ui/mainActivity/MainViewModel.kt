package com.example.currencyapp.ui.mainActivity

import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(networkConnectivityObserver: NetworkConnectivityObserver) :
    BaseViewModel(networkConnectivityObserver)