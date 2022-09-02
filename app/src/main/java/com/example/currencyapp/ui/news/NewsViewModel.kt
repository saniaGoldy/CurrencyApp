package com.example.currencyapp.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.repository.APIResponseProcessor
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.ui.news.mоdel.SearchSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val searchSettings = MutableLiveData(SearchSettings(null, null, null))

    private val _news = MutableLiveData<Result<List<Data>>>()
    val news: LiveData<Result<List<Data>>>
        get() = _news

    init{
        fetchNewsData()
    }

    private fun fetchNewsData(){
        repository.makeNewsQuery(object : APIResponseProcessor<List<Data>> {
            override fun process(result: Result<List<Data>>) {
                _news.value = result
            }
        })
    }
}