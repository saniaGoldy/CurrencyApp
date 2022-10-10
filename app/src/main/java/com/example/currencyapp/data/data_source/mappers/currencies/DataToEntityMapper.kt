package com.example.currencyapp.data.data_source.mappers.currencies

import com.example.currencyapp.data.data_source.mappers.Mapper
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.rates.CurrencyData

class DataToEntityMapper : Mapper<List<CurrencyData>, List<CurrencyDataEntity>> {

    override fun map(from: List<CurrencyData>): List<CurrencyDataEntity> {
        return from.map { it.toEntity() }
    }

    private fun CurrencyData.toEntity(): CurrencyDataEntity {
        return CurrencyDataEntity(
            currency.name,
            rate,
            rateStory ?: mapOf()
        )
    }
}