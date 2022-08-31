package com.example.currencyapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.model.CurrencyFluctuation

interface MainRepository {
    fun makeCurrencyQuery(processor: APIResponseProcessor<List<CurrencyFluctuation>>)

    fun makeNewsQuery(processor: APIResponseProcessor<List<Data>>)

    fun fetchDataFromLocalDB(): LiveData<List<CurrencyFluctuation>>

    fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>)
}