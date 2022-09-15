package com.example.currencyapp.data.repository.local

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.rates.CurrencyData
import java.io.IOException

class LocalDBRepositoryImpl(private val localDB: LocalDB) : LocalDBRepository {

    override suspend fun fetchCurrenciesList(): Result<List<CurrencyData>> {
        val currencies = localDB
            .currencyDao().getAll().map { entity ->
                CurrencyData(entity.iso4217Alpha, entity.rate, entity.rateStory)
            }

        Log.d(TAG, "fetchCurrenciesList FromLocalDB: $currencies")

        return currencies.let {
            if (it.isEmpty()) {
                Result.failure(IOException("failed to load data from local repository"))
            } else {
                Result.success(it)
            }
        }
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

    override suspend fun fetchCurrencyDataByCode(code: String): Result<CurrencyData> {
         val currencyEntity: CurrencyDataEntity = localDB.currencyDao().findById(code)
        return Result.success(CurrencyData(currencyEntity.iso4217Alpha, currencyEntity.rate, currencyEntity.rateStory))
    }
}