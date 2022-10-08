package com.example.currencyapp.ui.ratesList.ratesListFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyapp.LiveDataTestUtil
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.UpdatableData
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.usecases.rates.RatesListUseCase
import com.google.common.truth.Truth.assertThat
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
            UpdatableData(
                listOf(CurrencyData("UAH", 1.0, mapOf())), true
            )
        )
        viewModel.updateDataState()

        assertThat(
            with(LiveDataTestUtil<DataState<List<CurrencyData>>>()) {
                viewModel.ratesDataState.isValueGetsEmitted(emissionChecker = object :
                    LiveDataTestUtil.EmissionChecker {
                    override fun <T> check(value: T?): Boolean {
                        return value?.let {
                            val emission = value as DataState<List<CurrencyData>>
                            emission is DataState.Success<List<CurrencyData>>
                        } ?: false
                    }
                })
            }).isTrue()

    }

    @Test
    fun updateDataStateReturnsFailureWhenReceiveResultFailure() = runTest {
        coEvery { interactorMock.fetchRatesList() } returns Result.failure(IOException())

        viewModel.updateDataState()

        assertThat(
            with(LiveDataTestUtil<DataState<List<CurrencyData>>>()) {
                viewModel.ratesDataState.isValueGetsEmitted(emissionChecker = object :
                    LiveDataTestUtil.EmissionChecker {
                    override fun <T> check(value: T?): Boolean {
                        return value?.let {
                            val emission = value as DataState<List<CurrencyData>>
                            emission is DataState.Failure
                        } ?: false
                    }
                })
            }).isTrue()
    }

    @Test
    fun updateDataStateReturnsSuccessWhenReceiveSuccessWithMessage() = runTest {
        coEvery { interactorMock.fetchRatesList() } returns Result.success(
            UpdatableData(
                listOf(CurrencyData("UAH", 1.0, mapOf())), false
            )
        )
        viewModel.updateDataState()

        assertThat(
            with(LiveDataTestUtil<DataState<List<CurrencyData>>>()) {
                viewModel.ratesDataState.isValueGetsEmitted(emissionChecker = object :
                    LiveDataTestUtil.EmissionChecker {
                    override fun <T> check(value: T?): Boolean {
                        return value?.let {
                            val emission = value as DataState<List<CurrencyData>>
                            emission is DataState.Success
                        } ?: false
                    }
                })
            }).isTrue()
    }

    @Test
    fun updateDataStateReturnsLoadingImmediatelyAfterLoadCurrencyDataCalled() = runTest {
        coEvery { interactorMock.fetchRatesList() } returns Result.success(
            UpdatableData(
                listOf(CurrencyData("UAH", 1.0, mapOf())), true
            )
        )

        viewModel.updateDataState()

        assertThat(
            with(LiveDataTestUtil<DataState<List<CurrencyData>>>()) {
                viewModel.ratesDataState.isValueGetsEmitted(emissionChecker = object :
                    LiveDataTestUtil.EmissionChecker {
                    override fun <T> check(value: T?): Boolean {
                        return value?.let {
                            val emission = value as DataState<List<CurrencyData>>
                            emission is DataState.Loading
                        } ?: false
                    }
                })
            }).isTrue()
    }

}