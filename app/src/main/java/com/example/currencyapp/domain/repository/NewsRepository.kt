package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.news.NewsData
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun fetchNewsList(settings: SearchSettings): List<NewsData>

    fun loadNewsSettings(): Flow<SearchSettings>

    suspend fun saveNewsSettings(settings: SearchSettings)
}