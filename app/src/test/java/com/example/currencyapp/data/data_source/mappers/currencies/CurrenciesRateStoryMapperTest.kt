package com.example.currencyapp.data.data_source.mappers.currencies

import com.example.currencyapp.data.remote.entities.currencyRateStory.CurrenciesRateStory
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert
import org.junit.Before
import org.junit.Test

internal class CurrenciesRateStoryMapperTest {

    private lateinit var mapper: CurrenciesRateStoryMapper

    @Before
    fun setup() {
        mapper = CurrenciesRateStoryMapper()
    }

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
            mapper.map(
                CurrenciesRateStory(
                    "", "",
                    mapOf(
                        firstDate to mapOf(
                            firstCurrency to firstCurrencyRateAtDate1,
                            secondCurrency to secondCurrencyRateAtDate1
                        ),
                        secondDate to mapOf(
                            firstCurrency to firstCurrencyRateAtDate2,
                            secondCurrency to secondCurrencyRateAtDate2
                        )
                    ),
                    "",
                    success = true,
                    timeSeries = true
                )
            )
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
            mapper.map(
                CurrenciesRateStory(
                    "",
                    "",
                    mapOf(
                        firstDate to mapOf(
                            firstCurrency to firstCurrencyRateAtDate1
                        )
                    ),
                    "",
                    success = true,
                    timeSeries = true
                )
            )
            Assert.fail()
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .contains("No enum constant com.example.currencyapp.domain.model.rates.Currencies")
        }
    }
}