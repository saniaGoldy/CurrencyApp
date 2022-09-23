package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.data_source.remote.FakeRemoteDataSource
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.IOException

class FakeRatesRepository(remoteDataSource: FakeRemoteDataSource = FakeRemoteDataSource()) : RatesRepository {

    private var fetchCurrencyListShouldThrowException = false
    private var fetchCurrencyListShouldReturnEmptyList = false

    fun setFetchCurrencyListShouldThrowException(value: Boolean) {
        fetchCurrencyListShouldThrowException = value
    }

    fun setFetchCurrencyListShouldReturnEmptyList(value: Boolean) {
        fetchCurrencyListShouldReturnEmptyList = value
    }

    private val currencies = remoteDataSource.currencies

    private var settings = RatesListSettings()

    override suspend fun fetchCurrenciesList(): List<CurrencyData> {
        if (fetchCurrencyListShouldThrowException) {
            throw IOException()
        }
        return if (fetchCurrencyListShouldReturnEmptyList) listOf() else currencies
    }

    override suspend fun saveRatesListSettings(settings: RatesListSettings) {
        this.settings = settings
    }

    override fun loadRatesListSettings(): Flow<RatesListSettings> = flowOf(settings)

    /** available codes: UAH, USD */
    override suspend fun getCurrencyByCode(code: String): CurrencyData {
        return currencies.first { it.iso4217Alpha == code }
    }
}