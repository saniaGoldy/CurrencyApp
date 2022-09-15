package com.example.currencyapp.data.repository.remote

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.news.Data.Companion.DATE_TIME_DELIMITER
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.CurrentDateData
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.PublishDate
import com.example.currencyapp.domain.model.rates.CurrencyData
import java.io.IOException

class RemoteRepositoryImpl(
    private val currencyAPI: CurrencyAPI,
) : RemoteRepository {

    override suspend fun loadCurrencyList(baseCurrency: String): Result<List<CurrencyData>> {
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

            Result.success(currenciesData)

        } else {
            Result.failure(IOException(response.errorBody().toString()))
        }
    }

    override suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>> {
        val response = currencyAPI.getCurrencyNews(
            settings.tags,
            settings.keywords,
            settings.timeGap
        )

        return if (response.isSuccessful) {

            Result.success(response.body()!!.data.map {
                val publishedAt =
                    it.publishedAt.substringBefore(DATE_TIME_DELIMITER) to
                            it.publishedAt.substringAfter(DATE_TIME_DELIMITER)

                NewsData(
                    it.description,
                    PublishDate(publishedAt.first, publishedAt.second),
                    it.source,
                    it.tags,
                    it.title,
                    it.url
                )
            })
        } else {
            Result.failure(IOException(response.errorBody().toString()))
        }
    }
}