package com.example.currencyapp.domain.repository.rates

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.repository.local.LocalDBRepository
import com.example.currencyapp.data.repository.preferences.PreferencesRepository
import com.example.currencyapp.data.repository.remote.RemoteRepository
import com.example.currencyapp.domain.model.rates.Currencies
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RatesRepositoryUseCase @Inject constructor(
    private val localDBRepository: LocalDBRepository,
    private val preferencesRepository: PreferencesRepository,
    private val remoteRepository: RemoteRepository
) : RatesRepository {

    private var baseCurrency: String = Currencies.UAH.name
    private var baseCurrencyChanged = false


    override suspend fun fetchCurrenciesList(
    ): Result<List<CurrencyData>> {

        updateLocalDB()

        return localDBRepository.fetchCurrenciesList()
    }

    private suspend fun updateLocalDB() {
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
            fetchRatesFromRemote(baseCurrency).let { result ->

                if (result.isSuccess) {
                    when (isUpToDate) {
                        //It's null when app is started first time on the device
                        null -> {
                            localDBRepository.saveCurrenciesList(
                                result.getOrNull()!!
                            )
                        }
                        else -> {
                            localDBRepository.updateCurrenciesList(
                                result.getOrNull()!!
                            )
                        }
                    }

                }
            }
        }
    }

    private suspend fun fetchRatesFromRemote(
        baseCurrency: String
    ): Result<List<CurrencyData>> {
        preferencesRepository.saveRatesUpdateDate()
        return remoteRepository.loadCurrencyList(baseCurrency)
    }

    //TODO: call withContext(Dispatchers.IO) ?
    override suspend fun saveRatesListSettings(settings: RatesListSettings) {
        baseCurrencyChanged = settings.currencyCode != baseCurrency
        Log.d(TAG, "saveRatesListSettings baseCurrencyChanged: $baseCurrencyChanged")

        preferencesRepository.saveRatesListSettings(settings)
    }

    override fun loadRatesListSettings(): Flow<RatesListSettings> =
        preferencesRepository.loadRatesListSettings().map { ratesListSettings ->
            baseCurrency = ratesListSettings.currencyCode
            ratesListSettings
        }

    override suspend fun getCurrencyByCode(code: String): Result<CurrencyData> {
        return localDBRepository.fetchCurrencyDataByCode(code)
    }
}