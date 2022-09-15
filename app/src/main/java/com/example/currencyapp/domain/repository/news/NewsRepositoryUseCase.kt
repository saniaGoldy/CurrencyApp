package com.example.currencyapp.domain.repository.news

import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.data.repository.preferences.PreferencesRepository
import com.example.currencyapp.data.repository.remote.RemoteRepository
import com.example.currencyapp.domain.model.news.NewsData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val remoteRepository: RemoteRepository
) : NewsRepository {

    override suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>> {
        return remoteRepository.fetchNewsList(settings)
    }

    override fun loadNewsSettings(): Flow<SearchSettings> =
        preferencesRepository.loadNewsSettings()

    override suspend fun saveNewsSettings(settings: SearchSettings) {
        preferencesRepository.saveNewsSettings(settings)
    }
}