package com.example.currencyapp.domain.repository.rates

import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RatesRepository {

    suspend fun fetchCurrenciesList(): DataState<List<CurrencyData>>

    suspend fun saveRatesListSettings(settings: RatesListSettings)

    fun loadRatesListSettings(): Flow<RatesListSettings>

}