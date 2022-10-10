package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.data_source.remote.FakeRemoteDataSource
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.model.news.NewsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.IOException

class FakeNewsRepository(private val remoteDataSource: FakeRemoteDataSource = FakeRemoteDataSource()) :
    NewsRepository {

    private var fetchNewsListShouldThrowException = false

    fun setFetchNewsListShouldThrowException(value: Boolean) {
        fetchNewsListShouldThrowException = value
    }

    override suspend fun fetchNewsList(settings: SearchSettings): List<NewsData> {
        if (fetchNewsListShouldThrowException) {
            throw IOException()
        }
        return remoteDataSource.news
    }

    override fun loadNewsSettings(): Flow<SearchSettings> {
        return flowOf(SearchSettings())
    }

    override suspend fun saveNewsSettings(settings: SearchSettings) {
    }
}