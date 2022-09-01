package com.example.currencyapp.data.remote

import com.example.currencyapp.data.remote.entities.CurrenciesFluctuationsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyAPI {
    @Headers("apikey: VN1OrfB9k7ytPwwPR5rQzJmgJhxGBAM0")

    @GET("/exchangerates_data/fluctuation")
    suspend fun getCurrencyFluctuation(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String
    ): Response<CurrenciesFluctuationsResponse>
}