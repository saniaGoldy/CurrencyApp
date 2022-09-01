package com.example.currencyapp.domain.repository

import com.example.currencyapp.domain.model.CurrencyFluctuation

interface MainRepository {
    suspend fun makeCurrencyQuery(): Result<List<CurrencyFluctuation>>

    suspend fun fetchDataFromLocalDB(): List<CurrencyFluctuation>

    fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>)
}