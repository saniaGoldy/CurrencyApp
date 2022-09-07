package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.CurrencyFluctuation
import kotlinx.coroutines.CoroutineScope

interface MainRepository {
    suspend fun loadCurrencyList(): DataState<List<CurrencyFluctuation>>

    suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>>

    suspend fun fetchCurrenciesList(): DataState<List<CurrencyFluctuation>>

    fun saveCurrenciesList(currencies: List<CurrencyFluctuation>, scope: CoroutineScope)

    suspend fun loadSettings(): SearchSettings

    fun saveSettings(settings: SearchSettings, scope: CoroutineScope)

    sealed class DataState<out T: Any?>{
        object Loading: DataState<Nothing>()
        data class Success<out T: Any>(val result: T) : DataState<T>()
        data class Failure(val errorInfo: String = "Ooops") : DataState<Nothing>()
        object Default : DataState<Nothing>()
    }
}