package com.example.currencyapp.ui.news

import android.app.Application
import com.example.currencyapp.domain.usecases.news.NewsSettingsReadUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SearchSettingsBaseViewModel @Inject constructor(
    interactor: NewsSettingsReadUseCase,
    context: Application
) : BaseViewModel(context) {
    val searchSettings = interactor.fetchNewsSettings()
}