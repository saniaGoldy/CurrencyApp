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
import com.example.currencyapp.domain.repository.MainRepository.DataState
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: MainRepository,
    context: Application
) : BaseViewModel(context) {

    private val _searchSettings = MutableLiveData<SearchSettings>()
    val searchSettings get() = _searchSettings

    private val _newsDataState: MutableLiveData<DataState<List<Data>>> =
        MutableLiveData(DataState.Default)
    val newsDataState: LiveData<DataState<List<Data>>>
        get() = _newsDataState

    private val exceptionHandler = CoroutineExceptionHandler { context, error ->
        Log.d(TAG, error.toString())
        _newsDataState.postValue(DataState.Failure())
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            Log.d(TAG, "loadSettings: start")
            _searchSettings.postValue(repository.loadSettings())
            fetchNews()
        }
    }

    private fun fetchNews() {
        Log.d(TAG, "fetchNews")
        _newsDataState.value = DataState.Loading

        viewModelScope.launch(exceptionHandler) {
            _newsDataState.postValue(
                repository.fetchNewsList(
                    _searchSettings.value ?: SearchSettings()
                )
            )
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

            //load news with new settings
            fetchNews()

            repository.saveSettings(settings, viewModelScope)
        }
    }
}
