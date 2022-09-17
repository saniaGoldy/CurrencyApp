package com.example.currencyapp.ui.news.newsListFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.usecases.news.NewsListUseCase
import com.example.currencyapp.ui.news.SearchSettingsBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val interactor: NewsListUseCase,
    context: Application
) : SearchSettingsBaseViewModel(interactor, context) {

    private val _newsDataState: MutableLiveData<DataState<List<NewsData>>> =
        MutableLiveData(DataState.Default)
    val newsDataState: LiveData<DataState<List<NewsData>>>
        get() = _newsDataState

    fun fetchNews(settings: SearchSettings) {
        Log.d(TAG, "fetchNews")
        _newsDataState.value = DataState.Loading

        viewModelScope.launch {
            _newsDataState.postValue(
                interactor.fetchNewsList(settings)
                    .let {
                        if (it.isSuccess) {
                            DataState.Success(it.getOrNull()!!)
                        } else {
                            DataState.Failure(it.exceptionOrNull().toString())
                        }
                    }
            )
        }
    }
}
