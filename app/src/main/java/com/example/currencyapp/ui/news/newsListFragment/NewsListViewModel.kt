package com.example.currencyapp.ui.news.newsListFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.usecases.news.NewsListUseCase
import com.example.currencyapp.ui.news.SearchSettingsBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val interactor: NewsListUseCase,
    @ApplicationContext context: Context
) : SearchSettingsBaseViewModel(interactor, context) {

    private val _newsDataState: MutableLiveData<DataState<List<NewsData>>> =
        MutableLiveData(DataState.Default)
    val newsDataState: LiveData<DataState<List<NewsData>>>
        get() = _newsDataState

    fun fetchNews(settings: SearchSettings) {
        Log.d(TAG, "fetchNews")
        _newsDataState.value = DataState.Loading

        viewModelScope.launch {
            updateNewsDataState(settings)
        }
    }

    private suspend fun updateNewsDataState(settings: SearchSettings) {
        _newsDataState.postValue(
            getDataState(settings)
        )
    }

    private suspend fun getDataState(settings: SearchSettings): DataState<List<NewsData>> {
       interactor.fetchNewsList(settings).let { result ->
           return if (result.isSuccess) {
                    DataState.Success(result.getOrNull()!!)
                } else {
                    DataState.Failure(result.exceptionOrNull().toString())
                }
            }
    }
}
