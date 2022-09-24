package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.model.InconsistentData
import com.example.currencyapp.domain.repository.FakeRatesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


internal class RatesListUseCaseTest {

    private lateinit var useCase: RatesListUseCase
    private lateinit var repository: FakeRatesRepository


    @Before
    fun setup() {

        repository = FakeRatesRepository()
        useCase = RatesListUseCase(repository)
    }

    @Test
    fun `fetchRatesList returns success on non empty list`() = runBlocking {
        assertThat(useCase.fetchRatesList()).isEqualTo(repository.fetchCurrenciesList())
    }

    @Test
    fun `fetchRatesList returns success with error message on empty list`() = runBlocking {
        repository.setFetchCurrencyListShouldReturnEmptyList(true)
        assertThat(useCase.fetchRatesList()).isInstanceOf(InconsistentData.SuccessWithErrorInfo::class.java)
    }

    @Test
    fun `fetchRatesList returns failure on exception`() = runBlocking {
        repository.setFetchCurrencyListShouldThrowException(true)
        assertThat(useCase.fetchRatesList()).isInstanceOf(InconsistentData.Failure::class.java)
    }
}