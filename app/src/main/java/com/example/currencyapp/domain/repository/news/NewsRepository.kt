package com.example.currencyapp.domain.repository.news

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>>

    fun loadNewsSettings(): Flow<SearchSettings>

    fun saveNewsSettings(settings: SearchSettings, scope: CoroutineScope)
}