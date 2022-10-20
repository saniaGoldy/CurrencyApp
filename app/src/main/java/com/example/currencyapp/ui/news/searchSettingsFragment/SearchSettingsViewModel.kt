package com.example.currencyapp.ui.news.searchSettingsFragment

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.remote.entities.news.NewsApiRequestOptions
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.domain.usecases.news.NewsSettingsEditUseCase
import com.example.currencyapp.ui.news.SearchSettingsBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSettingsViewModel @Inject constructor(
    private val interactor: NewsSettingsEditUseCase,
    networkConnectivityObserver: NetworkConnectivityObserver
) : SearchSettingsBaseViewModel(interactor, networkConnectivityObserver) {

    fun setSearchSettings(
        keywords: String,
        tags: String,
        timeGapMode: NewsApiRequestOptions,
        from: String,
        to: String
    ) {
        val settings = SearchSettings(
            keywords,
            tags,
            getTimeGap(timeGapMode, from, to)
        )

        settings.timeGapMode = timeGapMode

        //We don`t want to write settings if they has not changed
        //(User opened settings dialog and clicked OK without actually changing settings)
        if (searchSettings.value != settings) {
            viewModelScope.launch { interactor.saveNewsSettings(settings) }
        }
    }

    private fun getTimeGap(
        timeGapMode: NewsApiRequestOptions,
        from: String,
        to: String
    ) = when (timeGapMode) {
        NewsApiRequestOptions.DateFrom -> from
        NewsApiRequestOptions.DateFromTo -> "$from,$to"
        else -> timeGapMode.queryParam
    }
}