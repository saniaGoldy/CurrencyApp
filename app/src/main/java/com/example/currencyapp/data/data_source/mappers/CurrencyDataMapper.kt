package com.example.currencyapp.data.data_source.mappers

import com.example.currencyapp.domain.model.rates.CurrencyData

object CurrencyDataMapper {
    fun mapToCurrencyData(rates: List<Pair<String, Map<String, Double>>>): MutableList<CurrencyData> {
        val currenciesData = mutableListOf<CurrencyData>()

        rates[0].second.toList().forEach { rate ->
            currenciesData.add(CurrencyData(rate.first, 1 / rate.second, null))
        }

        currenciesData.forEach { currencyData ->
            val dateToRateMap = mutableMapOf<String, Double>()

            rates.forEach { date ->
                date.second[currencyData.iso4217Alpha]?.let { rate ->
                    dateToRateMap[date.first] = 1 / rate
                }
            }

            currencyData.rateStory = dateToRateMap
        }

        return currenciesData
    }
}