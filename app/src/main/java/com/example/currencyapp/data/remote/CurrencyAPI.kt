package com.example.currencyapp.data.remote

import com.example.currencyapp.data.remote.entities.currencyFluctuation.CurrenciesFluctuationsResponse
import com.example.currencyapp.data.remote.entities.currencyRateStory.CurrenciesRateStory
import com.example.currencyapp.data.remote.entities.news.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("/exchangerates_data/fluctuation")
    suspend fun getCurrencyFluctuation(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String
    ): CurrenciesFluctuationsResponse

    @GET("/exchangerates_data/timeseries")
    suspend fun getCurrencyRates(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String
    ): CurrenciesRateStory

    /**
     @param [tags] Use this parameter to search for tags by which news are tagged.
     You can search by multiple tags, separating tags with a comma, you can also exclude tags that you do not want to appear in your search results.
     Example: &tags=Bitcoin, -money. Results will exclude tags money from results

     @param [keywords] Use this parameter to search for sentences, you can also exclude words that you do not want to appear in your search results. Example: To search for 'New bitcoin news 2021' but exclude 'Etherum': &sources=new bitcoin news 2021, -etherum
     */
    @GET("/financelayer/news")
    suspend fun getCurrencyNews(
        @Query("tags") tags: String,
        @Query("keywords") keywords: String,
        @Query("date") date: String
    ): NewsResponse
}