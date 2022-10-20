package com.example.currencyapp.data.data_source.preferences

import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.model.rates.RatesListSettings
import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    fun loadNewsSettings(): Flow<SearchSettings>

    suspend fun saveNewsSettings(settings: SearchSettings)

    suspend fun isRatesUpToDate(): Boolean?

    suspend fun saveRatesUpdateDate()

    fun loadRatesListSettings(): Flow<RatesListSettings>

    suspend fun saveRatesListSettings(settings: RatesListSettings)
}