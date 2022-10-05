package com.example.currencyapp.ui.ratesList.ratesListFragment

import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.google.common.truth.Truth
import org.junit.Test

internal class RatesFilterTest {
    private val testData = listOf(
        CurrencyData("UAH", 1.0, mapOf("2022-09-21" to 1.0, "2022-09-20" to 1.0)),
        CurrencyData("USD", 36.933, mapOf("2022-09-21" to 36.933, "2022-09-20" to 36.851))
    )

    @Test
    fun filterReturnsEmptyListWhenCashedDataStateIsFailure() {
        val ratesFilter = RatesFilter(DataState.Failure())
        Truth.assertThat(ratesFilter.filter(null)).isEmpty()
    }

    @Test
    fun filterReturnsEmptyListWhenCashedDataStateIsLoading() {
        val ratesFilter = RatesFilter(DataState.Loading)
        Truth.assertThat(ratesFilter.filter(null)).isEmpty()
    }

    @Test
    fun filterReturnsCashedListWhenKeywordIsEmpty() {
        val ratesFilter = RatesFilter(DataState.Success(testData))
        Truth.assertThat(ratesFilter.filter("")).isEqualTo(testData)
    }

    @Test
    fun filterReturnsCashedListWhenKeywordIsNull() {
        val ratesFilter = RatesFilter(DataState.Success(testData))
        Truth.assertThat(ratesFilter.filter(null)).isEqualTo(testData)
    }

    @Test
    fun filterReturnsFilteredListWhenKeywordIsMatching() {
        val ratesFilter = RatesFilter(DataState.Success(testData))
        Truth.assertThat(ratesFilter.filter("uah")).isEqualTo(listOf(testData[0]))
    }

    @Test
    fun filterReturnsEmptyListWhenKeywordIsNotMatching() {
        val ratesFilter = RatesFilter(DataState.Success(testData))
        Truth.assertThat(ratesFilter.filter("5")).isEqualTo(listOf<NewsData>())
    }
}