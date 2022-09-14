package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>>

    suspend fun fetchCurrenciesList(scope: CoroutineScope): DataState<List<CurrencyData>>

    suspend fun loadNewsSettings(): SearchSettings

    fun saveNewsSettings(settings: SearchSettings, scope: CoroutineScope)

    fun saveRatesListSettings(settings: RatesListSettings, scope: CoroutineScope)

    fun loadRatesListSettings(): Flow<RatesListSettings>

    sealed class DataState<out T : Any?> {
        object Loading : DataState<Nothing>()
        data class Success<out T : Any>(val result: T) : DataState<T>()
        data class Failure(val errorInfo: String = "Ooops") : DataState<Nothing>()
        object Default : DataState<Nothing>()
    }
}