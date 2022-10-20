package com.example.currencyapp.domain.usecases.news

import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsListUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcher: CoroutineDispatcher
) : NewsSettingsReadUseCase(newsRepository) {

    suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>> {

        return withContext(dispatcher) {
            runCatching { newsRepository.fetchNewsList(settings) }
        }
    }
}