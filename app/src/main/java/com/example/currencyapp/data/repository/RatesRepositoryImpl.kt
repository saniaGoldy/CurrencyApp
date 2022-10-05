package com.example.currencyapp.data.repository

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.local.LocalDBDataSource
import com.example.currencyapp.data.data_source.preferences.PreferencesDataSource
import com.example.currencyapp.data.data_source.remote.RemoteDataSource
import com.example.currencyapp.domain.model.UpdatableData
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val localDBDataSource: LocalDBDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val remoteDataSource: RemoteDataSource
) : RatesRepository {

    override suspend fun fetchCurrenciesList(
    ): UpdatableData<List<CurrencyData>> {
        var isCurrenciesListIsUpdated = updateCurrenciesList()

        val data = localDBDataSource.fetchCurrenciesList()
            .also { if (it.isEmpty()) isCurrenciesListIsUpdated = false }

        return UpdatableData(data, isCurrenciesListIsUpdated)
    }

    /**
     * @return true if succeeded to update currencies list or if it is already up to date otherwise false
     */
    private suspend fun updateCurrenciesList(): Boolean {
        val storedSettings = getStoredSettings()
        val isUpToDate = preferencesDataSource.isRatesUpToDate()

        val isRatesIsUpToDateWithBaseCurrency = storedSettings.isRatesIsUpToDateWithSettings


        Log.d(
            TAG,
            "fetchCurrenciesList isRatesIsUpToDateWithBaseCurrency: $isRatesIsUpToDateWithBaseCurrency"
        )
        Log.d(TAG, "fetchCurrenciesList IsRatesUpToDate: $isUpToDate")

        if (isUpToDate != true || !isRatesIsUpToDateWithBaseCurrency) {
            return tryToUpdateLocalDB(storedSettings.currencyCode).also { isSucceeded ->
                updateRatesSettingsUpToDateStatus(isSucceeded, storedSettings)
            }
        }
        return true
    }

    private suspend fun updateRatesSettingsUpToDateStatus(
        isSucceededToUpdate: Boolean,
        storedSettings: RatesListSettings
    ) {
        if (isSucceededToUpdate) {
            storedSettings.isRatesIsUpToDateWithSettings = true
            saveRatesListSettings(storedSettings)
        }
    }

    private suspend fun getStoredSettings() = loadRatesListSettings().first()

    private suspend fun tryToUpdateLocalDB(baseCurrency: String): Boolean {
        return try {
            fetchRatesFromRemote(baseCurrency).let { result ->
                localDBDataSource.saveCurrenciesList(
                    result
                )
            }
            true
        } catch (e: Exception) {
            val message = "failed to fetch rates data from remote"
            Log.e(TAG, "updateLocalDB: $message")
            false
        }
    }

    private suspend fun fetchRatesFromRemote(
        baseCurrency: String
    ): List<CurrencyData> {
        preferencesDataSource.saveRatesUpdateDate()
        return remoteDataSource.loadCurrencyList(baseCurrency)
    }

    override suspend fun saveRatesListSettings(settings: RatesListSettings) {
        if (getStoredSettings().currencyCode != settings.currencyCode)
            settings.isRatesIsUpToDateWithSettings = false
        preferencesDataSource.saveRatesListSettings(settings)
    }

    override fun loadRatesListSettings(): Flow<RatesListSettings> =
        preferencesDataSource.loadRatesListSettings()

    override suspend fun getCurrencyByCode(code: String): CurrencyData {
        return localDBDataSource.fetchCurrencyDataByCode(code)
    }
}