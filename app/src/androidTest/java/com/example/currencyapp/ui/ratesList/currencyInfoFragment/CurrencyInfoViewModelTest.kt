package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyapp.LiveDataTestUtil
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.domain.usecases.rates.RatesCurrencyInfoUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
internal class CurrencyInfoViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    private lateinit var viewModel: CurrencyInfoViewModel
    private lateinit var interactorMock: RatesCurrencyInfoUseCase

    @Before
    fun setup() {
        interactorMock = mockk(relaxed = true)
        networkConnectivityObserver =
            NetworkConnectivityObserver(InstrumentationRegistry.getInstrumentation().targetContext)

        viewModel = CurrencyInfoViewModel(
            interactorMock,
            networkConnectivityObserver
        )
    }

    @Test
    fun loadCurrencyDataReturnsSuccessWhenReceiveResultSuccess() = runTest {

        val currencyData = CurrencyData("UAH", 1.0, mapOf())
        coEvery { interactorMock.getCurrencyByCode("") } returns Result.success(
            currencyData
        )
        viewModel.loadCurrencyData("UAH")

        assertThat(
            with(LiveDataTestUtil<DataState<CurrencyData>>()) {
                viewModel.currency.isValueGetsEmitted(
                    emissionChecker = object :
                        LiveDataTestUtil.EmissionChecker {
                        override fun <T> check(value: T?): Boolean {
                            return value?.let {
                                val emission = value as DataState<CurrencyData>
                                emission is DataState.Success<CurrencyData>
                            } ?: false
                        }
                    }
                )
            }
        ).isTrue()
    }

    @Test
    fun loadCurrencyDataReturnsFailureWhenReceiveResultFailure() = runTest {
        coEvery { interactorMock.getCurrencyByCode("") } returns Result.failure(
            IOException()
        )
        viewModel.loadCurrencyData("")

        assertThat(
            with(LiveDataTestUtil<DataState<CurrencyData>>()) {
                viewModel.currency.isValueGetsEmitted(
                    emissionChecker = object :
                        LiveDataTestUtil.EmissionChecker {
                        override fun <T> check(value: T?): Boolean {
                            return value?.let {
                                val emission = value as DataState<CurrencyData>
                                emission is DataState.Failure
                            } ?: false
                        }
                    }
                )
            }
        ).isTrue()
    }

    @Test
    fun loadCurrencyDataReturnsLoadingImmediatelyAfterLoadCurrencyDataCalled() = runTest {
        coEvery { interactorMock.getCurrencyByCode("") } returns Result.failure(IOException())

        viewModel.loadCurrencyData("")

        assertThat(
            with(LiveDataTestUtil<DataState<CurrencyData>>()) {
                viewModel.currency.isValueGetsEmitted(
                    emissionChecker = object :
                        LiveDataTestUtil.EmissionChecker {
                        override fun <T> check(value: T?): Boolean {
                            return value?.let {
                                val emission = value as DataState<CurrencyData>
                                emission is DataState.Loading
                            } ?: false
                        }
                    }
                )
            }
        ).isTrue()
    }
}