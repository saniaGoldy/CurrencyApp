package com.example.currencyapp.data.data_source.local

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.mapCurrencyDataToEntity
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.mapEntityToCurrencyData
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.toCurrencyData
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.domain.model.rates.CurrencyData

class LocalDBDataSourceImpl(private val localDB: LocalDB) : LocalDBDataSource {

    override suspend fun fetchCurrenciesList(): List<CurrencyData> {
        val currencies = localDB
            .currencyDao().getAll().mapEntityToCurrencyData()

        Log.d(TAG, "fetchCurrenciesList FromLocalDB: $currencies")

        return currencies
    }


    override suspend fun saveCurrenciesList(currencies: List<CurrencyData>) {
        localDB.currencyDao().insertAll(currencies.mapCurrencyDataToEntity()).also {
            Log.d(
                TAG,
                "Saving data to local db: $currencies"
            )
        }
    }

    override suspend fun updateCurrenciesList(currencies: List<CurrencyData>) {
        localDB.currencyDao().update(currencies.mapCurrencyDataToEntity()).also {
            Log.d(
                TAG,
                "Updating data in local db: $currencies"
            )
        }
    }

    override suspend fun fetchCurrencyDataByCode(code: String): CurrencyData =
        localDB.currencyDao().findById(code).toCurrencyData()
}