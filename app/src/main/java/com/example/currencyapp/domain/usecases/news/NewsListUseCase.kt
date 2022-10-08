package com.example.currencyapp.domain.usecases.news

import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsListUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) : NewsSettingsReadUseCase(newsRepository) {

    suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>> {

        return withContext(Dispatchers.IO) {
            runCatching { newsRepository.fetchNewsList(settings) }
        }
    }
}