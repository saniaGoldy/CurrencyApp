package com.example.currencyapp.data.data_source.remote

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.mapToCurrencyData
import com.example.currencyapp.data.data_source.mappers.NewsDataMapper.mapToNewsData
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.CurrentDateData
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.rates.CurrencyData

class RemoteDataSourceImpl(
    private val currencyAPI: CurrencyAPI,
) : RemoteDataSource {

    override suspend fun loadCurrencyList(baseCurrency: String): List<CurrencyData> {
        return currencyAPI.getCurrencyRates(
            CurrentDateData.startDate,
            CurrentDateData.currentDate,
            baseCurrency
        ).mapToCurrencyData().also { Log.d(TAG, "loadCurrencyList: $it") }
    }

    override suspend fun fetchNewsList(settings: SearchSettings): List<NewsData> {
        return currencyAPI.getCurrencyNews(
            settings.tags,
            settings.keywords,
            settings.timeGap
        ).data.mapToNewsData()
    }
}