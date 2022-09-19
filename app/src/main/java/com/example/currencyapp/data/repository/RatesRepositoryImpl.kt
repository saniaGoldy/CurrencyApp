package com.example.currencyapp.data.repository

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.local.LocalDBDataSource
import com.example.currencyapp.data.data_source.preferences.PreferencesDataSource
import com.example.currencyapp.data.data_source.remote.RemoteDataSource
import com.example.currencyapp.domain.model.rates.Currencies
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val localDBDataSource: LocalDBDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val remoteDataSource: RemoteDataSource
) : RatesRepository {

    private var baseCurrency: String = Currencies.UAH.name
    private var baseCurrencyChanged = false


    override suspend fun fetchCurrenciesList(
    ): List<CurrencyData> {

        updateLocalDB()

        return localDBDataSource.fetchCurrenciesList()
    }

    private suspend fun updateLocalDB() {
        val isUpToDate = preferencesDataSource.isRatesUpToDate()

        if (isUpToDate.also {
                Log.d(
                    TAG,
                    "fetchCurrenciesList IsRatesUpToDate: $it"
                )
            } != true || baseCurrencyChanged.also {
                Log.d(
                    TAG,
                    "fetchCurrenciesList isCurrencyChanged: $it"
                )
            }) {

            baseCurrencyChanged = false

            try {
                fetchRatesFromRemote(baseCurrency).let { result ->
                    when (isUpToDate) {
                        //It's null when app is started first time on the device
                        null -> {
                            localDBDataSource.saveCurrenciesList(
                                result
                            )
                        }
                        else -> {
                            localDBDataSource.updateCurrenciesList(
                                result
                            )
                        }
                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "updateLocalDB: failed to fetch rates data from remote")
            }
        }
    }

    private suspend fun fetchRatesFromRemote(
        baseCurrency: String
    ): List<CurrencyData> {
        preferencesDataSource.saveRatesUpdateDate()
        return remoteDataSource.loadCurrencyList(baseCurrency)
    }

    override suspend fun saveRatesListSettings(settings: RatesListSettings) {
        baseCurrencyChanged = settings.currencyCode != baseCurrency
        Log.d(TAG, "saveRatesListSettings baseCurrencyChanged: $baseCurrencyChanged")

        preferencesDataSource.saveRatesListSettings(settings)
    }

    override fun loadRatesListSettings(): Flow<RatesListSettings> =
        preferencesDataSource.loadRatesListSettings().map { ratesListSettings ->
            baseCurrency = ratesListSettings.currencyCode
            ratesListSettings
        }

    override suspend fun getCurrencyByCode(code: String): CurrencyData {
        return localDBDataSource.fetchCurrencyDataByCode(code)
    }
}