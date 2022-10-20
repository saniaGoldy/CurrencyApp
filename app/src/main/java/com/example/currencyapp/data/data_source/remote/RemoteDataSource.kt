package com.example.currencyapp.data.data_source.remote

import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.rates.CurrencyData

interface RemoteDataSource {

    suspend fun loadCurrencyList(baseCurrency: String): List<CurrencyData>

    suspend fun fetchNewsList(settings: SearchSettings): List<NewsData>
}