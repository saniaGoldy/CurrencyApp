package com.example.currencyapp.data.data_source.mappers

import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.mapCurrencyDataToEntity
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.mapEntityToCurrencyData
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.mapToCurrencyData
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.toCurrencyData
import com.example.currencyapp.data.data_source.mappers.CurrencyDataMapper.toEntity
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.example.currencyapp.data.remote.entities.currencyRateStory.CurrenciesRateStory
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.fail
import org.junit.Test

internal class CurrencyDataMapperTest {

    @Test
    fun `map CurrenciesRateStory to CurrencyData with correct input data`() {

        val firstCurrency = "UAH"
        val secondCurrency = "USD"
        val firstDate = "date1"
        val secondDate = "date2"
        val firstCurrencyRateAtDate1 = 1.2
        val secondCurrencyRateAtDate1 = 1.0
        val firstCurrencyRateAtDate2 = 1.5
        val secondCurrencyRateAtDate2 = 0.3


        assertThat(
            CurrenciesRateStory(
                "", "", mapOf(
                    firstDate to mapOf(
                        firstCurrency to firstCurrencyRateAtDate1,
                        secondCurrency to secondCurrencyRateAtDate1
                    ),
                    secondDate to mapOf(
                        firstCurrency to firstCurrencyRateAtDate2,
                        secondCurrency to secondCurrencyRateAtDate2
                    )
                ), "",
                success = true,
                timeSeries = true
            ).mapToCurrencyData()
        ).isEqualTo(
            mutableListOf(
                CurrencyData(
                    firstCurrency,
                    1 / firstCurrencyRateAtDate1,
                    mapOf(
                        firstDate to 1 / firstCurrencyRateAtDate1,
                        secondDate to 1 / firstCurrencyRateAtDate2
                    )
                ),
                CurrencyData(
                    secondCurrency,
                    1 / secondCurrencyRateAtDate1,
                    mapOf(
                        firstDate to 1 / secondCurrencyRateAtDate1,
                        secondDate to 1 / secondCurrencyRateAtDate2
                    )
                )
            )
        )
    }

    @Test
    fun `map CurrenciesRateStory to CurrencyData with invalid currencyCodes throws exception`() {
        val firstCurrency = "InvalidCurrencyCode1"
        val firstDate = "date1"
        val firstCurrencyRateAtDate1 = 1.2

        try {
            CurrenciesRateStory(
                "",
                "",
                mapOf(
                    firstDate to mapOf(
                        firstCurrency to firstCurrencyRateAtDate1
                    )
                ), "",
                success = true,
                timeSeries = true
            ).mapToCurrencyData()
            fail()
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .contains("No enum constant com.example.currencyapp.domain.model.rates.Currencies")
        }
    }

    @Test
    fun mapCurrencyDataToEntity() {
        val currencyData = listOf(CurrencyData("UAH", 1.0, null))
        assertThat(currencyData.mapCurrencyDataToEntity()).isEqualTo(
            listOf(
                CurrencyDataEntity(
                    "UAH",
                    1.0,
                    mapOf()
                )
            )
        )
    }

    @Test
    fun `map db entities to CurrencyData`() {
        val currencyDataEntities = listOf(CurrencyDataEntity("UAH", 1.0, mapOf()))
        assertThat(currencyDataEntities.mapEntityToCurrencyData()).isEqualTo(
            listOf(
                CurrencyData(
                    "UAH",
                    1.0,
                    mapOf()
                )
            )
        )
    }

    @Test
    fun `converting CurrencyData to db entity`() {
        assertThat(
            CurrencyData(
                "UAH",
                1.0,
                null
            ).toEntity()
        ).isEqualTo(
            CurrencyDataEntity(
                "UAH",
                1.0,
                mapOf()
            )
        )
    }

    @Test
    fun `converting db entity to CurrencyData`() {
        assertThat(
            CurrencyDataEntity(
                "UAH",
                1.0,
                mapOf()
            ).toCurrencyData()
        ).isEqualTo(
            CurrencyData(
                "UAH",
                1.0,
                mapOf()
            )
        )
    }

}