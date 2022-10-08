package com.example.currencyapp.data.data_source.mappers.currencies

import com.example.currencyapp.data.data_source.mappers.Mapper
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.rates.Currencies
import com.example.currencyapp.domain.model.rates.CurrencyData

class EntityToDataMapper : Mapper<List<CurrencyDataEntity>, List<CurrencyData>> {

    override fun map(from: List<CurrencyDataEntity>): List<CurrencyData> {
        return from.map { entity ->
            entity.toCurrencyData()
        }
    }
}

fun CurrencyDataEntity.toCurrencyData(): CurrencyData {
    return CurrencyData(Currencies.valueOf(this.iso4217Alpha), this.rate, this.rateStory)
}