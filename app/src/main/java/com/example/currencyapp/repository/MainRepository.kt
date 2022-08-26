package com.example.currencyapp.repository

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.example.currencyapp.currencyAPI.RetrofitInstance
import com.example.currencyapp.currencyAPI.entities.CurrenciesFluctuationsResponse
import com.example.currencyapp.model.Currencies
import com.example.currencyapp.model.CurrencyFluctuation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

object MainRepository {
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


    fun makeCurrencyQuery(processor: ResponseProcessor) {
        RetrofitInstance.currencyAPI.getCurrencyFluctuation(
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

    interface ResponseProcessor {
        fun process(result: Result<List<CurrencyFluctuation>>)
    }

}