package com.example.currencyapp.currencyAPI.entities

data class CurrenciesFluctuationsResponse(
    val base: String,
    val end_date: String,
    val fluctuation: Boolean,
    val rates: Map<String, Currency>,
    val start_date: String,
    val success: Boolean
)