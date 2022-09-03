package com.example.currencyapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.model.CurrencyFluctuation

interface MainRepository {
    suspend fun makeCurrencyQuery(): Result<List<CurrencyFluctuation>>

    suspend fun makeNewsQuery(): Result<List<Data>>

    suspend fun fetchDataFromLocalDB(): List<CurrencyFluctuation>

    fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>)
}