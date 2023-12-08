package com.example.currencyapp.data.remote.entities.currencyRateStory

import com.google.gson.annotations.SerializedName

data class CurrenciesRateStory(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timeseries")
    val timeSeries: Boolean,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Map<String, Map<String, Double>>
)