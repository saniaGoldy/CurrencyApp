package com.example.currencyapp.data.data_source.mappers.currencies

import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

internal class DataToEntityMapperTest {

    private lateinit var mapper: DataToEntityMapper

    @Before
    fun setup() {
        mapper = DataToEntityMapper()
    }

    @Test
    fun mapCurrencyDataToEntity() {
        val currencyData = listOf(CurrencyData("UAH", 1.0, null))
        assertThat(mapper.map(currencyData)).isEqualTo(
            listOf(
                CurrencyDataEntity(
                    "UAH",
                    1.0,
                    mapOf()
                )
            )
        )
    }
}