package com.example.currencyapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.currencyapp.domain.model.CurrencyFluctuation
import retrofit2.Response

interface MainRepository {
    suspend fun makeCurrencyQuery(): Result<List<CurrencyFluctuation>>

    suspend fun fetchDataFromLocalDB(): List<CurrencyFluctuation>

    fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>)
}