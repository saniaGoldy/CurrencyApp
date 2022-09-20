package com.example.currencyapp.ui.news.searchSettingsFragment

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.remote.entities.news.NewsApiRequestOptions
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.usecases.news.NewsSettingsEditUseCase
import com.example.currencyapp.ui.news.SearchSettingsBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSettingsViewModel @Inject constructor(
    private val interactor: NewsSettingsEditUseCase,
    context: Application
) : SearchSettingsBaseViewModel(interactor, context) {

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
            when (timeGapMode) {
                NewsApiRequestOptions.DateFrom -> from
                NewsApiRequestOptions.DateFromTo -> "$from,$to"
                else -> timeGapMode.queryParam
            }
        )

        settings.timeGapMode = timeGapMode

        if (searchSettings.value != settings) {
            viewModelScope.launch { interactor.saveNewsSettings(settings) }
        }
    }
}