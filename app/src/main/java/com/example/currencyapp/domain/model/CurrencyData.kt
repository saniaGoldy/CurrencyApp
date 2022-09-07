package com.example.currencyapp.domain.model

import com.example.currencyapp.data.repository.CurrentDateData

data class CurrencyData(
    val iso4217Alpha: String,
    val rate: Double,
    var rateStory: Map<String, Double>?
) {

    val fullName: String = Currencies.valueOf(iso4217Alpha).fullName

    fun getRateDifference(): Double {
        val startRate = rateStory?.get(CurrentDateData.yesterdaysDate)
        val endRate = rateStory?.get(CurrentDateData.currentDate)
        return if (startRate != null && endRate != null) {
            val diff = endRate - startRate
            diff
        } else 0.0
    }
}