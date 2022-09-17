package com.example.currencyapp.domain.repository

import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import kotlinx.coroutines.flow.Flow

interface RatesRepository {

    suspend fun fetchCurrenciesList(): List<CurrencyData>

    suspend fun saveRatesListSettings(settings: RatesListSettings)

    fun loadRatesListSettings(): Flow<RatesListSettings>

    suspend fun getCurrencyByCode(code: String): CurrencyData
}