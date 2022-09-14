package com.example.currencyapp.data.repository.preferences

import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun loadNewsSettings(): Flow<SearchSettings>

    suspend fun saveNewsSettings(settings: SearchSettings)

    suspend fun isRatesUpToDate(): Boolean?

    suspend fun saveRatesUpdateDate()

    fun loadRatesListSettings(): Flow<RatesListSettings>

    suspend fun saveRatesListSettings(settings: RatesListSettings)
}