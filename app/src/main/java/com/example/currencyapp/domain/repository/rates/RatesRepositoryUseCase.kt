package com.example.currencyapp.domain.repository.rates

import android.app.Application
import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.repository.LocalDBRepository
import com.example.currencyapp.data.repository.PreferencesRepository
import com.example.currencyapp.data.repository.RemoteRepository
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RatesRepositoryUseCase @Inject constructor(
    localDB: LocalDB,
    context: Application,
    currencyAPI: CurrencyAPI
) : RatesRepository {

    private val localDBRepository = LocalDBRepository(localDB)
    private val preferencesRepository = PreferencesRepository(context)
    private val remoteRepository = RemoteRepository(currencyAPI)

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
            fetchRatesFromRemote(baseCurrency).apply {

                if (this is DataState.Success) {
                    return when (isUpToDate) {
                        //It's null when app is started first time on the device
                        null -> {
                            localDBRepository.saveCurrenciesList(
                                this.result,
                                scope
                            )
                        }
                        else -> {
                            localDBRepository.updateCurrenciesList(
                                this.result,
                                scope
                            )
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

        preferencesRepository.saveRatesListSettings(settings, scope)
    }

    override fun loadRatesListSettings(): Flow<RatesListSettings> =
        preferencesRepository.loadRatesListSettings().map { ratesListSettings ->
            baseCurrency = ratesListSettings.currencyCode
            ratesListSettings
        }
}