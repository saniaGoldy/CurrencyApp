package com.example.currencyapp.data.repository

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LocalDBRepository(private val localDB: LocalDB) {
    suspend fun fetchCurrenciesList(): MainRepository.DataState<List<CurrencyData>> {
        val currencies = localDB
            .currencyDao().getAll().map { entity ->
                CurrencyData(entity.iso4217Alpha, entity.rate, entity.rateStory)
            }

        Log.d(TAG, "fetchCurrenciesList FromLocalDB: $currencies")

        return if (currencies.isEmpty()) MainRepository.DataState.Failure() else MainRepository.DataState.Success(
            currencies
        )
    }


    fun saveCurrenciesList(currencies: List<CurrencyData>, scope: CoroutineScope): Job {
        return scope.launch(Dispatchers.IO) {
            localDB
                .currencyDao().insertAll(currencies.map {
                    CurrencyDataEntity(
                        it.iso4217Alpha,
                        it.rate,
                        it.rateStory ?: mapOf()
                    )
                }).also {
                    Log.d(
                        TAG,
                        "Saving data to local db: $currencies"
                    )
                }
        }
    }

    fun updateCurrenciesList(currencies: List<CurrencyData>, scope: CoroutineScope): Job {
        return scope.launch(Dispatchers.IO) {
            localDB
                .currencyDao().update(currencies.map {
                    CurrencyDataEntity(
                        it.iso4217Alpha,
                        it.rate,
                        it.rateStory ?: mapOf()
                    )
                }).also {
                    Log.d(
                        TAG,
                        "Updating data in local db: $currencies"
                    )
                }
        }
    }
}