package com.example.currencyapp.data.data_source.mappers

import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.data.remote.entities.currencyRateStory.CurrenciesRateStory
import com.example.currencyapp.domain.model.rates.Currencies
import com.example.currencyapp.domain.model.rates.CurrencyData

object CurrencyDataMapper {
    fun CurrenciesRateStory.mapToCurrencyData(): MutableList<CurrencyData> {
        val currenciesData = mutableListOf<CurrencyData>()
        val rates = this.rates.toList()

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

    fun List<CurrencyData>.mapCurrencyDataToEntity(): List<CurrencyDataEntity> {
        return this.map {
            it.toEntity()
        }
    }

    fun List<CurrencyDataEntity>.mapEntityToCurrencyData(): List<CurrencyData> {
        return this.map { entity ->
            entity.toCurrencyData()
        }
    }

    fun CurrencyData.toEntity(): CurrencyDataEntity {
        return CurrencyDataEntity(
            this.currency.name,
            this.rate,
            this.rateStory ?: mapOf()
        )
    }

    fun CurrencyDataEntity.toCurrencyData(): CurrencyData {
        return CurrencyData(Currencies.valueOf(this.iso4217Alpha), this.rate, this.rateStory)
    }
}