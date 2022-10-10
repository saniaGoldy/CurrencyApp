package com.example.currencyapp.data.data_source.remote

import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.PublishDate
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.model.rates.CurrencyData
import java.io.IOException

class FakeRemoteDataSource : RemoteDataSource {

    private var loadCurrencyListShouldThrowException = false

    fun setLoadCurrencyListShouldThrowException(value: Boolean) {
        loadCurrencyListShouldThrowException = value
    }

    private var fetchNewsListShouldThrowException = false

    fun setFetchNewsListShouldThrowException(value: Boolean) {
        loadCurrencyListShouldThrowException = value
    }

    internal val currencies = listOf(
        CurrencyData("UAH", 1.0, mapOf("2022-09-21" to 1.0, "2022-09-20" to 1.0)),
        CurrencyData("USD", 36.933, mapOf("2022-09-21" to 36.933, "2022-09-20" to 36.851))
    )

    internal val news = listOf(
        NewsData(
            "some news description",
            PublishDate("2022-09-21", "2:02"),
            "Plumberg",
            listOf(),
            "breaking news!",
            "some url"
        )
    )

    override suspend fun loadCurrencyList(baseCurrency: String): List<CurrencyData> {
        if (loadCurrencyListShouldThrowException) {
            throw IOException()
        }
        return currencies
    }

    override suspend fun fetchNewsList(settings: SearchSettings): List<NewsData> {
        if (fetchNewsListShouldThrowException)
            throw IOException()
        return news
    }
}