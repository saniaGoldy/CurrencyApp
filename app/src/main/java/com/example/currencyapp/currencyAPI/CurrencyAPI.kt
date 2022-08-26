package com.example.currencyapp.currencyAPI

import com.example.currencyapp.currencyAPI.entities.CurrenciesFluctuationsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyAPI {
    @Headers("apikey: VN1OrfB9k7ytPwwPR5rQzJmgJhxGBAM0")

    @GET("/exchangerates_data/fluctuation")
    fun getCurrencyFluctuation(
        @Query("start_date") startDate:String,
        @Query("end_date") endDate: String,
        @Query("base") base: String
    ): Call<CurrenciesFluctuationsResponse>
}