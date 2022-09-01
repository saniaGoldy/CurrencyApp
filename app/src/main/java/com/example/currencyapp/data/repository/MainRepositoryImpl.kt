package com.example.currencyapp.data.repository

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.CurrenciesFluctuationsResponse
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.MainRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val currencyAPI: CurrencyAPI,
    private val localDB: LocalDB
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


    override suspend fun makeCurrencyQuery(): Result<List<CurrencyFluctuation>> {
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
                        1 / it.value.end_rate,
                        1 / it.value.end_rate - 1 / it.value.start_rate
                    )
                }

            Result.success(currencies)
        } else {

            Result.failure(IOException(response.errorBody().toString()))
        }
    }

    override suspend fun fetchDataFromLocalDB(): List<CurrencyFluctuation> {
        return localDB
            .currencyDao().getAll().also {
                Log.d(
                    TAG,
                    "fetchDataFromLocalDB"
                )
            }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>) {
        GlobalScope.launch(Dispatchers.IO) {
            localDB
                .currencyDao().insertAll(currencies).also {
                    Log.d(
                        TAG,
                        "Saving data to local db..."
                    )
                }
        }
    }

}