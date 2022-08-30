package com.example.currencyapp.domain.repository

interface MainRepository {
    fun makeCurrencyQuery(processor: IResponseProcessor)
}