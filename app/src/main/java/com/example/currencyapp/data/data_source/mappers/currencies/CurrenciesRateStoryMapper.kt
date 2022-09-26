package com.example.currencyapp.data.data_source.mappers.currencies

import com.example.currencyapp.data.data_source.mappers.Mapper
import com.example.currencyapp.data.remote.entities.currencyRateStory.CurrenciesRateStory
import com.example.currencyapp.domain.model.rates.Currencies
import com.example.currencyapp.domain.model.rates.CurrencyData

class CurrenciesRateStoryMapper: Mapper<CurrenciesRateStory, MutableList<CurrencyData>> {

    override fun map(from: CurrenciesRateStory): MutableList<CurrencyData> {
        val currenciesData = mutableListOf<CurrencyData>()
        val rates = from.rates.toList()

        rates[0].second.toList().forEach { rate ->
            currenciesData.add(CurrencyData(Currencies.valueOf(rate.first), 1 / rate.second, null))
        }

        currenciesData.forEach { currencyData ->
            val dateToRateMap = mutableMapOf<String, Double>()

            rates.forEach { date ->
                date.second[currencyData.currency.name]?.let { rate ->
                    dateToRateMap[date.first] = 1 / rate
                }
            }

            currencyData.rateStory = dateToRateMap
        }

        return currenciesData
    }
}