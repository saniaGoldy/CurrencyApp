package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.model.UpdatableData
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.FakeRatesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


internal class RatesListUseCaseTest {

    private lateinit var useCase: RatesListUseCase
    private lateinit var repository: FakeRatesRepository

    @Before
    fun setup() {

        repository = FakeRatesRepository()
        useCase = RatesListUseCase(repository, Dispatchers.IO)
    }

    @Test
    fun `fetchRatesList returns success on non empty list`() = runBlocking {
        assertThat(useCase.fetchRatesList().isSuccess).isEqualTo(true)
    }

    @Test
    fun `fetchRatesList returns success with isUpToDate property set to false`() = runBlocking {
        repository.setFetchCurrencyListShouldReturnEmptyList(true)
        val data = useCase.fetchRatesList()
        assertThat(data.isSuccess).isEqualTo(true)
        assertThat(data.getOrNull()).isEqualTo(UpdatableData<List<CurrencyData>>(listOf(), false))
    }

    @Test
    fun `fetchRatesList returns failure on exception`() = runBlocking {
        repository.setFetchCurrencyListShouldThrowException(true)
        assertThat(useCase.fetchRatesList().isFailure).isEqualTo(true)
    }
}