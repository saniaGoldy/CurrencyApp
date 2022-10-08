package com.example.currencyapp.ui.news

import android.content.Context
import com.example.currencyapp.domain.usecases.news.NewsSettingsReadUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
open class SearchSettingsBaseViewModel @Inject constructor(
    interactor: NewsSettingsReadUseCase,
    @ApplicationContext context: Context
) : BaseViewModel(context) {
    val searchSettings = interactor.fetchNewsSettings()
}