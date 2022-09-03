package com.example.currencyapp.data.remote.entities.currencyFluctuation

import com.google.gson.annotations.SerializedName

data class CurrenciesFluctuationsResponse(
    val base: String,
    @SerializedName("end_date")
    val endDate: String,
    val fluctuation: Boolean,
    val rates: Map<String, Currency>,
    @SerializedName("start_date")
    val startDate: String,
    val success: Boolean
)