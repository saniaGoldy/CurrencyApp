package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.CurrencyData
import kotlinx.coroutines.CoroutineScope

interface MainRepository {

    suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>>

    suspend fun fetchCurrenciesList(scope: CoroutineScope): DataState<List<CurrencyData>>

    fun saveCurrenciesList(currencies: List<CurrencyData>, scope: CoroutineScope)

    suspend fun loadSettings(): SearchSettings

    fun saveSettings(settings: SearchSettings, scope: CoroutineScope)

    sealed class DataState<out T : Any?> {
        object Loading : DataState<Nothing>()
        data class Success<out T : Any>(val result: T) : DataState<T>()
        data class Failure(val errorInfo: String = "Ooops") : DataState<Nothing>()
        object Default : DataState<Nothing>()
    }
}