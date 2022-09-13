package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.CoroutineScope

interface MainRepository {

    suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>>

    suspend fun fetchCurrenciesList(scope: CoroutineScope, baseCurrency: String): DataState<List<CurrencyData>>

    fun saveCurrenciesList(currencies: List<CurrencyData>, scope: CoroutineScope)

    suspend fun loadNewsSettings(): SearchSettings

    fun saveNewsSettings(settings: SearchSettings, scope: CoroutineScope)

    fun saveRatesListSettings(settings: RatesListSettings, scope: CoroutineScope)

    suspend fun loadRatesListSettings(): RatesListSettings

    sealed class DataState<out T : Any?> {
        object Loading : DataState<Nothing>()
        data class Success<out T : Any>(val result: T) : DataState<T>()
        data class Failure(val errorInfo: String = "Ooops") : DataState<Nothing>()
        object Default : DataState<Nothing>()
    }
}