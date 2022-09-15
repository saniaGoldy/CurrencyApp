package com.example.currencyapp.data.repository.remote

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.rates.CurrencyData

interface RemoteRepository {

    suspend fun loadCurrencyList(baseCurrency: String): Result<List<CurrencyData>>

    suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>>
}