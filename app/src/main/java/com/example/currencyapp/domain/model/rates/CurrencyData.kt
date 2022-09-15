package com.example.currencyapp.domain.model.rates


data class CurrencyData(
    val iso4217Alpha: String,
    val rate: Double,
    var rateStory: Map<String, Double>?
) {

    val fullName: String = Currencies.valueOf(iso4217Alpha).fullName

    fun getRateDifference(): Double {
        val rates = rateStory?.values?.toList()
        val startRate = rates?.get(0)
        val endRate = rates?.get(1)
        return if (startRate != null && endRate != null) {
            endRate - startRate
        } else 0.0
    }

}