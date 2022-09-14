package com.example.currencyapp.data.repository.remote

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState

interface RemoteRepository {
    suspend fun loadCurrencyList(baseCurrency: String): DataState<List<CurrencyData>>

    suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>>
}