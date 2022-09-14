package com.example.currencyapp.data.repository.local

import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface LocalDBRepository {
    suspend fun fetchCurrenciesList(): DataState<List<CurrencyData>>

    fun saveCurrenciesList(currencies: List<CurrencyData>, scope: CoroutineScope): Job

    fun updateCurrenciesList(currencies: List<CurrencyData>, scope: CoroutineScope): Job
}