package com.example.currencyapp.domain.repository.rates

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.repository.local.LocalDBRepository
import com.example.currencyapp.data.repository.preferences.PreferencesRepository
import com.example.currencyapp.data.repository.remote.RemoteRepository
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesRepositoryUseCase @Inject constructor(
    private val localDBRepository: LocalDBRepository,
    private val preferencesRepository: PreferencesRepository,
    private val remoteRepository: RemoteRepository
) : RatesRepository {

    private var baseCurrency: String = Currencies.UAH.name
    private var baseCurrencyChanged = false


    override suspend fun fetchCurrenciesList(
        scope: CoroutineScope,
    ): DataState<List<CurrencyData>> {

        updateLocalDB(scope)?.join()

        return localDBRepository.fetchCurrenciesList()
    }

    private suspend fun updateLocalDB(scope: CoroutineScope): Job? {
        val isUpToDate = preferencesRepository.isRatesUpToDate()

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
            fetchRatesFromRemote(baseCurrency).let { dataState ->

                if (dataState is DataState.Success) {
                    return scope.launch {
                        when (isUpToDate) {
                            //It's null when app is started first time on the device
                            null -> {
                                localDBRepository.saveCurrenciesList(
                                    dataState.result
                                )
                            }
                            else -> {
                                localDBRepository.updateCurrenciesList(
                                    dataState.result
                                )
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private suspend fun fetchRatesFromRemote(
        baseCurrency: String
    ): DataState<List<CurrencyData>> {
        preferencesRepository.saveRatesUpdateDate()
        return remoteRepository.loadCurrencyList(baseCurrency)
    }

    override fun saveRatesListSettings(settings: RatesListSettings, scope: CoroutineScope) {
        baseCurrencyChanged = settings.currencyCode != baseCurrency
        Log.d(TAG, "saveRatesListSettings baseCurrencyChanged: $baseCurrencyChanged")

        scope.launch(Dispatchers.IO) { preferencesRepository.saveRatesListSettings(settings) }
    }

    override fun loadRatesListSettings(): Flow<RatesListSettings> =
        preferencesRepository.loadRatesListSettings().map { ratesListSettings ->
            baseCurrency = ratesListSettings.currencyCode
            ratesListSettings
        }
}