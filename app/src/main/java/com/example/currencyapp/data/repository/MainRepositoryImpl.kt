package com.example.currencyapp.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.dataStore
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.MainRepository
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val currencyAPI: CurrencyAPI,
    private val localDB: LocalDB,
    private val context: Application
) : MainRepository {

    private val baseCurrency: String = Currencies.UAH.name

    @SuppressLint("SimpleDateFormat")
    private val dataFormat = SimpleDateFormat("yyyy-MM-dd")

    private val currentDate: String
        get() = dataFormat.format(Calendar.getInstance().time)
    private val yesterdaysDate: String
        get() {
            val calendar = Calendar.getInstance().apply { add(Calendar.DATE, -1) }
            return dataFormat.format(calendar.time)
        }


    override suspend fun fetchCurrencyList(): Result<List<CurrencyFluctuation>> {
        val response = currencyAPI.getCurrencyFluctuation(
            yesterdaysDate,
            currentDate,
            baseCurrency
        )

        return if (response.isSuccessful && response.body()?.success == true) {
            val currencies: List<CurrencyFluctuation> =
                response.body()!!.rates.entries.map {
                    CurrencyFluctuation(
                        it.key,
                        1 / it.value.endRate,
                        1 / it.value.endRate - 1 / it.value.startRate
                    )
                }

            Result.success(currencies)
        } else {
            Result.failure(IOException(response.errorBody().toString()))
        }
    }

    override suspend fun fetchNewsList(settings: SearchSettings): Result<List<Data>> {
        val response = currencyAPI.getCurrencyNews(
            settings.tags,
            settings.keywords,
            settings.timeGap
        )

        return if (response.isSuccessful) {
            Result.success(response.body()!!.data)
        } else {
            Result.failure(IOException(response.errorBody().toString()))
        }
    }

    override suspend fun fetchCurrenciesList(): List<CurrencyFluctuation> {
        return localDB
            .currencyDao().getAll().also {
                Log.d(
                    TAG,
                    "fetchCurrenciesList"
                )
            }
    }


    override fun saveCurrenciesList(currencies: List<CurrencyFluctuation>, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            localDB
                .currencyDao().insertAll(currencies).also {
                    Log.d(
                        TAG,
                        "Saving data to local db..."
                    )
                }
        }
    }

    private val gson = Gson()

    override suspend fun loadSettings(): SearchSettings {
        val settingsJson = context.dataStore.data.first()[SETTINGS_PREF_KEY] ?: ""
        return gson.fromJson(settingsJson, SearchSettings::class.java)
    }


    override fun saveSettings(settings: SearchSettings, scope:CoroutineScope) {
        Log.d(TAG, "saveSettings")
        scope.launch(Dispatchers.IO) {
            context.dataStore.edit {
                it[SETTINGS_PREF_KEY] = gson.toJson(settings)
                Log.d(TAG, "saveSettings: ${it[SETTINGS_PREF_KEY]}")
            }
        }
    }

    companion object {
        private val SETTINGS_PREF_KEY = stringPreferencesKey("searchSettings")
    }
}