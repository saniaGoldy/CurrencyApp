package com.example.currencyapp.domain.usecases.news

import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsSettingsEditUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcher: CoroutineDispatcher
) : NewsSettingsReadUseCase(newsRepository) {

    suspend fun saveNewsSettings(settings: SearchSettings) {
        withContext(dispatcher) {
            newsRepository.saveNewsSettings(settings)
        }
    }
}