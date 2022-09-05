package com.example.currencyapp.ui.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.NewsApiRequestOptions
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: MainRepository,
    context: Application
) : BaseViewModel(context) {

    private val _searchSettings = MutableLiveData<SearchSettings>()
    val searchSettings get() = _searchSettings

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "loadSettings: start")
            searchSettings.postValue(repository.loadSettingsFromPrefs().also {
                Log.d(
                    TAG,
                    "settingsLoading: $it"
                ) })
        }
    }

    private val _news = MutableLiveData<Result<List<Data>>>()

    val news: LiveData<Result<List<Data>>>
        get() = _news


    val isLoading = MutableLiveData(false)

    val areNewsUpToDate = MutableLiveData(false)

    fun fetchNews() {
        if (networkStatus.value == ConnectivityObserver.Status.Available)
            isLoading.value = true

        try {
            viewModelScope.launch(Dispatchers.IO) {
                _news.postValue(repository.makeNewsQuery(_searchSettings.value ?: SearchSettings()))
            }
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "fetchNews: ${e.printStackTrace()}")
        }
    }

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

        if (_searchSettings.value != settings) {
            _searchSettings.value = settings
            areNewsUpToDate.value = false
            repository.saveSettingsToPrefs(settings)
        }
    }

}