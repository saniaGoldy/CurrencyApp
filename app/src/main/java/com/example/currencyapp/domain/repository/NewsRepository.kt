package com.example.currencyapp.domain.repository

import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.SearchSettings
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun fetchNewsList(settings: SearchSettings): List<NewsData>

    fun loadNewsSettings(): Flow<SearchSettings>

    suspend fun saveNewsSettings(settings: SearchSettings)
}