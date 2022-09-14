package com.example.currencyapp.domain.repository.rates

import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RatesRepository {

    suspend fun fetchCurrenciesList(scope: CoroutineScope): DataState<List<CurrencyData>>

    fun saveRatesListSettings(settings: RatesListSettings, scope: CoroutineScope)

    fun loadRatesListSettings(): Flow<RatesListSettings>

}