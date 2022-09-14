package com.example.currencyapp.data.repository.local

import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface LocalDBRepository {
    suspend fun fetchCurrenciesList(): DataState<List<CurrencyData>>

    suspend fun saveCurrenciesList(currencies: List<CurrencyData>)

    suspend fun updateCurrenciesList(currencies: List<CurrencyData>)
}