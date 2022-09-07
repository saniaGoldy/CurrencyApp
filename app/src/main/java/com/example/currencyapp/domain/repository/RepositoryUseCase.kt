package com.example.currencyapp.domain.repository

import android.app.Application
import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.data.repository.LocalDBRepository
import com.example.currencyapp.data.repository.PreferencesRepository
import com.example.currencyapp.data.repository.RemoteRepository
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.repository.MainRepository.DataState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class RepositoryUseCase @Inject constructor(
    localDB: LocalDB,
    context: Application,
    currencyAPI: CurrencyAPI
) : MainRepository {

    private val localDBRepository = LocalDBRepository(localDB)
    private val preferencesRepository = PreferencesRepository(context)
    private val remoteRepository = RemoteRepository(currencyAPI)

    override suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>> {
        return remoteRepository.fetchNewsList(settings)
    }

    override suspend fun fetchCurrenciesList(scope: CoroutineScope): DataState<List<CurrencyData>> {
        return if (preferencesRepository.isRatesUpToDate().also {
                Log.d(
                    TAG,
                    "fetchCurrenciesList IsRatesUpToDate: $it"
                )
            }) {
            localDBRepository.fetchCurrenciesList()
        } else {
            preferencesRepository.saveRatesUpdateDate()

            val currencyData = remoteRepository.loadCurrencyList().apply {
                if (this is DataState.Success)
                    localDBRepository.saveCurrenciesList(
                        this.result,
                        scope
                    )
            }

            currencyData
        }
    }

    override fun saveCurrenciesList(currencies: List<CurrencyData>, scope: CoroutineScope) =
        localDBRepository.saveCurrenciesList(currencies, scope)

    override suspend fun loadSettings(): SearchSettings = preferencesRepository.loadSettings()

    override fun saveSettings(settings: SearchSettings, scope: CoroutineScope) =
        preferencesRepository.saveSettings(settings, scope)
}