package com.example.currencyapp.ui.news

import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.domain.usecases.news.NewsSettingsReadUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SearchSettingsBaseViewModel @Inject constructor(
    interactor: NewsSettingsReadUseCase,
    networkConnectivityObserver: NetworkConnectivityObserver
) : BaseViewModel(networkConnectivityObserver) {
    val searchSettings = interactor.fetchNewsSettings()
}