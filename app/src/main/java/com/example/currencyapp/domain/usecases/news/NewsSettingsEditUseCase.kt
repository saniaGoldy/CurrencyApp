package com.example.currencyapp.domain.usecases.news

import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsSettingsEditUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) : NewsSettingsReadUseCase(newsRepository) {

    suspend fun saveNewsSettings(settings: SearchSettings) {
        withContext(Dispatchers.IO) {
            newsRepository.saveNewsSettings(settings)
        }
    }
}