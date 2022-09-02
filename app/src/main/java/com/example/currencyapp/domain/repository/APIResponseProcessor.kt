package com.example.currencyapp.domain.repository

interface APIResponseProcessor<T> {
    fun process(result: Result<T>)
}