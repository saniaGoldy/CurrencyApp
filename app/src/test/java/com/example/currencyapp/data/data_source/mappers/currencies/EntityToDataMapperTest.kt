package com.example.currencyapp.data.data_source.mappers.currencies

import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

internal class EntityToDataMapperTest {

    private lateinit var mapper: EntityToDataMapper

    @Before
    fun setup() {
        mapper = EntityToDataMapper()
    }

    @Test
    fun `map db entities to CurrencyData`() {
        val currencyDataEntities = listOf(CurrencyDataEntity("UAH", 1.0, mapOf()))
        assertThat(mapper.map(currencyDataEntities)).isEqualTo(
            listOf(
                CurrencyData(
                    "UAH",
                    1.0,
                    mapOf()
                )
            )
        )
    }
}