package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.CurrencyFluctuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun fetchCurrencyList(): Result<List<CurrencyFluctuation>>

    suspend fun fetchNewsList(settings: SearchSettings): Result<List<Data>>

    suspend fun fetchCurrenciesList(): List<CurrencyFluctuation>

    fun saveCurrenciesList(currencies: List<CurrencyFluctuation>, scope: CoroutineScope)

    suspend fun loadSettings(): SearchSettings

    fun saveSettings(settings: SearchSettings, scope: CoroutineScope)
}