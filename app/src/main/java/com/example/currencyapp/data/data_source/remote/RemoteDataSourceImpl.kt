package com.example.currencyapp.data.data_source.remote

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.mappers.Mapper
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.currencyRateStory.CurrenciesRateStory
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.CurrentDateData
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.rates.CurrencyData

class RemoteDataSourceImpl(
    private val currencyAPI: CurrencyAPI,
    private val newsDataMapper: Mapper<List<Data>, List<NewsData>>,
    private val rateStoryMapper: Mapper<CurrenciesRateStory, MutableList<CurrencyData>>
) : RemoteDataSource {

    override suspend fun loadCurrencyList(baseCurrency: String): List<CurrencyData> {
        return rateStoryMapper.map(
            currencyAPI.getCurrencyRates(
                CurrentDateData.startDate,
                CurrentDateData.currentDate,
                baseCurrency
            )
        ).also { Log.d(TAG, "loadCurrencyList: $it") }
    }

    override suspend fun fetchNewsList(settings: SearchSettings): List<NewsData> {
        return newsDataMapper.map(
            currencyAPI.getCurrencyNews(
                settings.tags,
                settings.keywords,
                settings.timeGap
            ).data
        )
    }
}