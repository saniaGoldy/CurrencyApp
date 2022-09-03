package com.example.currencyapp.domain.repository

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.data.remote.entities.news.SearchSettings

interface MainRepository {
    suspend fun makeCurrencyQuery(): Result<List<CurrencyFluctuation>>

    suspend fun makeNewsQuery(settings: SearchSettings): Result<List<Data>>

    suspend fun fetchDataFromLocalDB(): List<CurrencyFluctuation>

    fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>)
}