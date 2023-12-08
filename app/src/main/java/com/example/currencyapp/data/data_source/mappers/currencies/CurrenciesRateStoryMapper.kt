package com.example.currencyapp.data.data_source.mappers.currencies

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.data.data_source.mappers.Mapper
import com.example.currencyapp.data.remote.entities.currencyRateStory.CurrenciesRateStory
import com.example.currencyapp.domain.model.rates.Currencies
import com.example.currencyapp.domain.model.rates.CurrencyData

class CurrenciesRateStoryMapper : Mapper<CurrenciesRateStory, MutableList<CurrencyData>> {

    override fun map(from: CurrenciesRateStory): MutableList<CurrencyData> {
        val rates = from.rates.toList()
        val currenciesData = getCurrenciesWithoutRateStories(rates)

        currenciesData.fillTheRateStories(rates)

        return currenciesData
    }

    private fun MutableList<CurrencyData>.fillTheRateStories(
        rates: List<Pair<String, Map<String, Double>>>
    ) {
        this.forEach { currencyData ->
            val dateToRateMap = mutableMapOf<String, Double>()

            rates.forEach { date ->
                dateToRateMap.addDateToRateEntry(date, currencyData)
            }

            currencyData.rateStory = dateToRateMap
        }
    }

    private fun MutableMap<String, Double>.addDateToRateEntry(
        date: Pair<String, Map<String, Double>>,
        currencyData: CurrencyData,
    ) {
        date.second[currencyData.currency.name]?.let { rate ->
            this[date.first] = 1 / rate
        }
    }

    private fun getCurrenciesWithoutRateStories(
        rates: List<Pair<String, Map<String, Double>>>
    ): MutableList<CurrencyData> {
        val currenciesData = mutableListOf<CurrencyData>()
        rates[0].second.toList().forEach { rate ->
            kotlin.runCatching { getCurrencyData(rate) }.also {
                it.exceptionOrNull()?.let {
                    Log.e(
                        TAG,
                        "failed to parse currency type: ${it.message}",
                    )
                }
            }.getOrNull()?.let {
                currenciesData.add(it)
            }
        }
        return currenciesData
    }

    private fun getCurrencyData(rate: Pair<String, Double>) =
        CurrencyData(Currencies.valueOf(rate.first), 1 / rate.second, null)
}