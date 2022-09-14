package com.example.currencyapp.data.repository.local

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState

class LocalDBRepositoryImpl(private val localDB: LocalDB) : LocalDBRepository {
    override suspend fun fetchCurrenciesList(): DataState<List<CurrencyData>> {
        val currencies = localDB
            .currencyDao().getAll().map { entity ->
                CurrencyData(entity.iso4217Alpha, entity.rate, entity.rateStory)
            }

        Log.d(TAG, "fetchCurrenciesList FromLocalDB: $currencies")

        return if (currencies.isEmpty()) DataState.Failure() else DataState.Success(
            currencies
        )
    }


    override suspend fun saveCurrenciesList(currencies: List<CurrencyData>) {
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

    override suspend fun updateCurrenciesList(currencies: List<CurrencyData>) {

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