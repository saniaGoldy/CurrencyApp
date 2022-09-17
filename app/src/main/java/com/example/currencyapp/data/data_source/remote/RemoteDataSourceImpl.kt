package com.example.currencyapp.data.data_source.remote

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper
import com.example.currencyapp.data.data_source.mappers.NewsDataMapper
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.CurrentDateData
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.rates.CurrencyData
import java.io.IOException

class RemoteDataSourceImpl(
    private val currencyAPI: CurrencyAPI,
) : RemoteDataSource {

    override suspend fun loadCurrencyList(baseCurrency: String): Result<List<CurrencyData>> {
        val response = currencyAPI.getCurrencyRates(
            CurrentDateData.startDate,
            CurrentDateData.currentDate,
            baseCurrency
        )

        return if (response.isSuccessful && response.body()?.success == true) {
            Result.success(
                CurrencyDataMapper.mapToCurrencyData(response.body()!!.rates.toList())
                    .also { Log.d(TAG, "loadCurrencyList: $it") })
        } else {
            Result.failure(IOException(response.code().toString()))
        }
    }

    override suspend fun fetchNewsList(settings: SearchSettings): Result<List<NewsData>> {
        val response = currencyAPI.getCurrencyNews(
            settings.tags,
            settings.keywords,
            settings.timeGap
        )

        return if (response.isSuccessful) {
            Result.success(NewsDataMapper.mapToNewsData(response.body()!!.data))
        } else {
            Result.failure(IOException(response.code().toString()))
        }
    }
}