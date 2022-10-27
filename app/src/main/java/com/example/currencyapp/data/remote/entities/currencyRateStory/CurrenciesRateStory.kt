package com.example.currencyapp.data.remote.entities.currencyRateStory

import com.google.gson.annotations.SerializedName

data class CurrenciesRateStory(
    @SerializedName("base")
    val base: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("rates")
    val rates: Map<String, Map<String, Double>>,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timeseries")
    val timeSeries: Boolean
)