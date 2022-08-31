package com.example.currencyapp.data.repository

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.currencyapp.TAG
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.currencyFluctuation.CurrenciesFluctuationsResponse
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.NewsResponse
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.APIResponseProcessor
import com.example.currencyapp.domain.repository.MainRepository
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


    override fun makeCurrencyQuery(processor: APIResponseProcessor<List<CurrencyFluctuation>>) {
        currencyAPI.getCurrencyFluctuation(
            yesterdaysDate,
            currentDate,
            baseCurrency
        ).enqueue(object :
            Callback<CurrenciesFluctuationsResponse> {
            override fun onResponse(
                call: Call<CurrenciesFluctuationsResponse>,
                response: Response<CurrenciesFluctuationsResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val currencies: List<CurrencyFluctuation> =
                        response.body()!!.rates.entries.map {
                            CurrencyFluctuation(
                                it.key,
                                1 / it.value.end_rate,
                                1 / it.value.end_rate - 1 / it.value.start_rate
                            )
                        }
                    processor.process(Result.success(currencies))
                } else {
                    processor.process(Result.failure(IOException("bad response")))
                }
            }

            override fun onFailure(call: Call<CurrenciesFluctuationsResponse>, t: Throwable) {
                processor.process(Result.failure(t))
            }

        })
    }

    override fun makeNewsQuery(processor: APIResponseProcessor<List<Data>>) {
        currencyAPI.getCurrencyNews("UAH", "Exchange rate")
            .enqueue(object :
                Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        processor.process(Result.success(response.body()!!.data))
                    } else {
                        processor.process(Result.failure(IOException(response.errorBody().toString())))
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    processor.process(Result.failure(t))
                }

            })
    }


    override fun fetchDataFromLocalDB(): LiveData<List<CurrencyFluctuation>> {
        return localDB
            .currencyDao().getAll().also {
                Log.d(
                    TAG,
                    "fetchDataFromLocalDB"
                )
            }
    }

    override fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>) {
        localDB
            .currencyDao().insertAll(currencies).also {
                Log.d(
                    TAG,
                    "Saving data to local db..."
                )
            }
    }

}