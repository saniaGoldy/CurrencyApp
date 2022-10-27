package com.example.currencyapp.ui.news.webPageActivity

import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    networkConnectivityObserver: NetworkConnectivityObserver
) : BaseViewModel(networkConnectivityObserver)