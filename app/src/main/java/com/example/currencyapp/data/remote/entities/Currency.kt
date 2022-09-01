package com.example.currencyapp.data.remote.entities

import com.google.gson.annotations.SerializedName

data class Currency(
    val change: Double,
    @SerializedName("change_pct")
    val changePct: Double,
    @SerializedName("end_rate")
    val endRate: Double,
    @SerializedName("start_rate")
    val startRate: Double
)
