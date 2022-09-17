package com.example.currencyapp.data.data_source.remote

import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.rates.CurrencyData

interface RemoteDataSource {

    suspend fun loadCurrencyList(baseCurrency: String): Result<List<CurrencyData>>

    suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>>
}