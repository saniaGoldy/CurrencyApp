package com.example.currencyapp.ui.ratesList.ratesListFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.usecases.rates.RatesListUseCase
import com.example.currencyapp.getOrAwaitValue
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
internal class RatesListViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RatesListViewModel
    private lateinit var interactorMock: RatesListUseCase

    @Before
    fun setup() {
        interactorMock = mockk(relaxed = true)

        viewModel = RatesListViewModel(
            interactorMock,
            InstrumentationRegistry.getInstrumentation().targetContext
        )
    }

    @Test
    fun updateDataStateReturnsSuccessWhenReceiveResultSuccess() = runTest {

        coEvery { interactorMock.fetchRatesList() } returns Result.success(
            listOf(CurrencyData("UAH", 1.0, mapOf()))
        )
        viewModel.updateDataState()

        //TODO: Fix this mf
        Truth.assertThat(viewModel.ratesDataState.getOrAwaitValue(valueCountdown = 2)).isInstanceOf(DataState.Success::class.java)
    }

    @Test
    fun updateDataStateReturnsFailureWhenReceiveResultFailure() = runTest {
        coEvery { interactorMock.fetchRatesList() } returns Result.failure(
            IOException()
        )
        viewModel.updateDataState()

        Truth.assertThat(viewModel.ratesDataState.getOrAwaitValue(valueCountdown = 2)).isInstanceOf(DataState.Failure::class.java)
    }

    @Test
    fun updateDataStateReturnsLoadingImmediatelyAfterLoadCurrencyDataCalled() = runTest {
        coEvery { interactorMock.fetchRatesList() } returns Result.failure(IOException())

        viewModel.updateDataState()

        Truth.assertThat(viewModel.ratesDataState.getOrAwaitValue()).isInstanceOf(DataState.Loading::class.java)
    }
}