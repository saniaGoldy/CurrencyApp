package com.example.currencyapp.data.data_source.local

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.mappers.Mapper
import com.example.currencyapp.data.data_source.mappers.currencies.toCurrencyData
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.rates.CurrencyData

class LocalDBDataSourceImpl(
    private val localDB: LocalDB,
    private val entityToCurrencyDataMapper: Mapper<List<CurrencyDataEntity>, List<CurrencyData>>,
    private val currencyDataToEntityMapper: Mapper<List<CurrencyData>, List<CurrencyDataEntity>>
) : LocalDBDataSource {

    override suspend fun fetchCurrenciesList(): List<CurrencyData> {
        val currencies = entityToCurrencyDataMapper.map(
            localDB
                .currencyDao().getAll()
        )

        Log.d(TAG, "fetchCurrenciesList FromLocalDB: $currencies")

        return currencies
    }

    override suspend fun saveCurrenciesList(currencies: List<CurrencyData>) {
        localDB.currencyDao().insertAll(currencyDataToEntityMapper.map(currencies)).also {
            Log.d(
                TAG,
                "Saving data to local db: $currencies"
            )
        }
    }

    override suspend fun fetchCurrencyDataByCode(code: String): CurrencyData =
        localDB.currencyDao().findById(code).toCurrencyData()
}