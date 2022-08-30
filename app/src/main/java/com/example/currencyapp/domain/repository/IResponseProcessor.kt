package com.example.currencyapp.domain.repository

import com.example.currencyapp.domain.model.CurrencyFluctuation

interface IResponseProcessor {
    fun process(result: Result<List<CurrencyFluctuation>>)
}