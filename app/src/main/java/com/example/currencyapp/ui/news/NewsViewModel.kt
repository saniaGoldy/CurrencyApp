package com.example.currencyapp.ui.news

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: MainRepository,
    context: Application
) : BaseViewModel(context) {

    val searchSettings = MutableLiveData<SearchSettings>()


    private val _news = MutableLiveData<Result<List<Data>>>()

    val news: LiveData<Result<List<Data>>>
        get() = _news


    init {
        fetchNews()
    }

    fun fetchNews() {
        if (networkStatus.value == ConnectivityObserver.Status.Available)
            viewModelScope.launch(Dispatchers.IO) {
                _news.postValue(repository.makeNewsQuery(searchSettings.value ?: SearchSettings()))
            }
    }

}