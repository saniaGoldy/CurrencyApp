package com.example.currencyapp.domain.repository.news

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>>

    fun loadNewsSettings(): Flow<SearchSettings>

    suspend fun saveNewsSettings(settings: SearchSettings)
}