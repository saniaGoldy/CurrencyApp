package com.example.currencyapp.data.repository

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.CurrentDateData
import com.example.currencyapp.domain.model.Currencies
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.repository.MainRepository.DataState

class RemoteRepository(
    private val currencyAPI: CurrencyAPI,
) {

    suspend fun loadCurrencyList(baseCurrency: String): DataState<List<CurrencyData>> {
        val response = currencyAPI.getCurrencyRates(
            CurrentDateData.startDate,
            CurrentDateData.currentDate,
            baseCurrency
        )

        return if (response.isSuccessful && response.body()?.success == true) {
            val currenciesData = mutableListOf<CurrencyData>()

            val rates: List<Pair<String, Map<String, Double>>> = response.body()!!.rates.toList()


            rates[0].second.toList().forEach { rate ->
                currenciesData.add(CurrencyData(rate.first, 1 / rate.second, null))
            }

            currenciesData.forEach { currencyData ->
                val dateToRateMap = mutableMapOf<String, Double>()

                rates.forEach { date ->
                    date.second[currencyData.iso4217Alpha]?.let { rate ->
                        dateToRateMap[date.first] = 1 / rate
                    }
                }

                currencyData.rateStory = dateToRateMap
            }

            Log.d(TAG, "loadCurrencyList: $currenciesData")

            DataState.Success(currenciesData)

        } else {
            DataState.Failure(response.errorBody().toString())
        }
    }

    suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>> {
        val response = currencyAPI.getCurrencyNews(
            settings.tags,
            settings.keywords,
            settings.timeGap
        )

        return if (response.isSuccessful) {
            DataState.Success(response.body()!!.data)
        } else {
            DataState.Failure(response.errorBody().toString())
        }
    }
}