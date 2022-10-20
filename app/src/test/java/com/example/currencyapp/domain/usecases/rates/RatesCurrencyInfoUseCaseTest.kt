package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.repository.FakeRatesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class RatesCurrencyInfoUseCaseTest {

    private lateinit var useCase: RatesCurrencyInfoUseCase
    private lateinit var repository: FakeRatesRepository

    @Before
    fun setup() {
        repository = FakeRatesRepository()
        useCase = RatesCurrencyInfoUseCase(repository, Dispatchers.IO)
    }

    @Test
    fun `getCurrencyByCode returns Success`() = runBlocking {

        val code = "UAH"
        assertThat(useCase.getCurrencyByCode(code)).isEqualTo(
            Result.success(
                repository.getCurrencyByCode(
                    code
                )
            )
        )
    }

    @Test
    fun `getCurrencyByCode returns Failure if catches exception`() = runBlocking {
        repository.setFetchCurrencyListShouldThrowException(true)
        val code = "UAH"
        assertThat(useCase.getCurrencyByCode(code)).isEqualTo(
            Result.success(
                repository.getCurrencyByCode(
                    code
                )
            )
        )
    }
}