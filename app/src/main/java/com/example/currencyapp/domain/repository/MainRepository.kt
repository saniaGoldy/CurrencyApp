package com.example.currencyapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.currencyapp.domain.model.CurrencyFluctuation

interface MainRepository {
    fun makeCurrencyQuery(processor: IResponseProcessor)

    fun fetchDataFromLocalDB(): LiveData<List<CurrencyFluctuation>>

    fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>)
}