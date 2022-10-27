package com.example.currencyapp.ui.news.newsListFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyapp.LiveDataTestUtil
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.domain.usecases.news.NewsListUseCase
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
internal class NewsListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private lateinit var viewModel: NewsListViewModel
    private lateinit var interactorMock: NewsListUseCase
    private lateinit var searchSettings: SearchSettings

    @Before
    fun setup() {
        interactorMock = mockk(relaxed = true)
        searchSettings = SearchSettings()
        networkConnectivityObserver =
            NetworkConnectivityObserver(InstrumentationRegistry.getInstrumentation().targetContext)

        viewModel = NewsListViewModel(
            interactorMock,
            networkConnectivityObserver
        )
    }

    @Test
    fun getNewsDataStateReturnsSuccessWhenReceiveList() = runTest {

        coEvery { interactorMock.fetchNewsList(SearchSettings()) } returns Result.success(
            listOf()
        )
        viewModel.fetchNews(searchSettings)

        assertThat(
            with(LiveDataTestUtil<DataState<List<NewsData>>>()) {
                viewModel.newsDataState.isValueGetsEmitted(
                    emissionChecker = object :
                        LiveDataTestUtil.EmissionChecker {
                        override fun <T> check(value: T?): Boolean {
                            return value?.let {
                                val emission = value as DataState<List<NewsData>>
                                emission is DataState.Success<List<NewsData>>
                            } ?: false
                        }
                    }
                )
            }
        ).isTrue()
    }

    @Test
    fun getNewsDataStateReturnsFailureWhenReceiveFailure() = runTest {
        coEvery { interactorMock.fetchNewsList(SearchSettings()) } returns Result.failure(
            IOException()
        )

        viewModel.fetchNews(searchSettings)

        assertThat(
            with(LiveDataTestUtil<DataState<List<NewsData>>>()) {
                viewModel.newsDataState.isValueGetsEmitted(
                    emissionChecker = object :
                        LiveDataTestUtil.EmissionChecker {
                        override fun <T> check(value: T?): Boolean {
                            return value?.let {
                                val emission = value as DataState<List<NewsData>>
                                emission is DataState.Failure
                            } ?: false
                        }
                    }
                )
            }
        ).isTrue()
    }

    @Test
    fun getNewsDataStateReturnsLoadingImmediatelyAfterFetchNewsCalled() = runTest {
        coEvery { interactorMock.fetchNewsList(SearchSettings()) } returns Result.success(listOf())

        viewModel.fetchNews(searchSettings)

        assertThat(
            with(LiveDataTestUtil<DataState<List<NewsData>>>()) {
                viewModel.newsDataState.isValueGetsEmitted(
                    emissionChecker = object :
                        LiveDataTestUtil.EmissionChecker {
                        override fun <T> check(value: T?): Boolean {
                            return value?.let {
                                val emission = value as DataState<List<NewsData>>
                                emission is DataState.Loading
                            } ?: false
                        }
                    }
                )
            }
        ).isTrue()
    }
}