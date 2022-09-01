package com.example.currencyapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.currencyapp.domain.model.CurrencyFluctuation

interface MainRepository {
    //fun makeCurrencyQuery(processor: IResponseProcessor)

    suspend fun fetchDataFromLocalDB(): List<CurrencyFluctuation>

    fun saveDataToLocalDB(currencies: List<CurrencyFluctuation>)
}