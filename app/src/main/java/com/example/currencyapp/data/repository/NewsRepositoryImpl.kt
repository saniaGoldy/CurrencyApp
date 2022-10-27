package com.example.currencyapp.data.repository

import com.example.currencyapp.data.data_source.preferences.PreferencesDataSource
import com.example.currencyapp.data.data_source.remote.RemoteDataSource
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.repository.NewsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val remoteDataSource: RemoteDataSource
) : NewsRepository {

    override suspend fun fetchNewsList(settings: SearchSettings): List<NewsData> {
        return remoteDataSource.fetchNewsList(settings)
    }

    override fun loadNewsSettings(): Flow<SearchSettings> =
        preferencesDataSource.loadNewsSettings()

    override suspend fun saveNewsSettings(settings: SearchSettings) {
        preferencesDataSource.saveNewsSettings(settings)
    }
}