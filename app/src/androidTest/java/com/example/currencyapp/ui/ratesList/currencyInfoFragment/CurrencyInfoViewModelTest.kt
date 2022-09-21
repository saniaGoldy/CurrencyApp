package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.usecases.rates.RatesCurrencyInfoUseCase
import com.example.currencyapp.getOrAwaitValue
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
internal class CurrencyInfoViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CurrencyInfoViewModel
    private lateinit var interactorMock: RatesCurrencyInfoUseCase

    @Before
    fun setup() {
        interactorMock = mockk(relaxed = true)

        viewModel = CurrencyInfoViewModel(
            interactorMock,
            InstrumentationRegistry.getInstrumentation().targetContext
        )
    }

    @Test
    fun loadCurrencyDataReturnsSuccessWhenReceiveResultSuccess() = runTest {

        val currencyData = CurrencyData("UAH", 1.0, mapOf())
        coEvery { interactorMock.getCurrencyByCode("") } returns Result.success(
            currencyData
        )
        viewModel.loadCurrencyData("UAH")


        Truth.assertThat(
            viewModel.currency.getOrAwaitValue(
                valueCountdown = 2
            )
        ).isInstanceOf(DataState.Success::class.java)
    }

    @Test
    fun loadCurrencyDataReturnsFailureWhenReceiveResultFailure() = runTest {
        coEvery { interactorMock.getCurrencyByCode("") } returns Result.failure(
            IOException()
        )
        viewModel.loadCurrencyData("")

        Truth.assertThat(viewModel.currency.getOrAwaitValue(valueCountdown = 2))
            .isInstanceOf(DataState.Failure::class.java)
    }

    @Test
    fun loadCurrencyDataReturnsLoadingImmediatelyAfterLoadCurrencyDataCalled() = runTest {
        coEvery { interactorMock.getCurrencyByCode("") } returns Result.failure(IOException())

        viewModel.loadCurrencyData("")

        Truth.assertThat(viewModel.currency.getOrAwaitValue()).isInstanceOf(DataState.Loading::class.java)
    }
}