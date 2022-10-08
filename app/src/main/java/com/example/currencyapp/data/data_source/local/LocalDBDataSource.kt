package com.example.currencyapp.data.data_source.local

import com.example.currencyapp.domain.model.rates.CurrencyData

interface LocalDBDataSource {

    suspend fun fetchCurrenciesList(): List<CurrencyData>

    suspend fun saveCurrenciesList(currencies: List<CurrencyData>)

    suspend fun fetchCurrencyDataByCode(code: String): CurrencyData
}