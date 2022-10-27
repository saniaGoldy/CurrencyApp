package com.example.currencyapp.domain.model.rates

data class CurrencyData(
    val currency: Currencies,
    val rate: Double,
    var rateStory: Map<String, Double>?
) {

    constructor (
        code: String,
        rate: Double,
        rateStory: Map<String, Double>?
    ) : this(Currencies.valueOf(code), rate, rateStory)

    fun getRateDifference(): Double {
        val rates = rateStory?.values?.toList()
        val startRate = rates?.getOrNull(0)
        val endRate = rates?.getOrNull(1)
        return if (startRate != null && endRate != null) {
            endRate - startRate
        } else 0.0
    }
}