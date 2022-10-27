package com.example.currencyapp.domain.usecases.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.repository.NewsRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler

open class NewsSettingsReadUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    private var exception: Throwable? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, error ->
        Log.d(TAG, error.toString())
        exception = error
    }

    fun fetchNewsSettings(): LiveData<SearchSettings> {
        return newsRepository.loadNewsSettings().asLiveData(exceptionHandler)
    }
}