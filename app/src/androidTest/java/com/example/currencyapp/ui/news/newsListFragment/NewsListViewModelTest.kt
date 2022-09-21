package com.example.currencyapp.ui.news.newsListFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.usecases.news.NewsListUseCase
import com.example.currencyapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
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
internal class NewsListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NewsListViewModel
    private lateinit var interactorMock: NewsListUseCase
    private lateinit var searchSettings: SearchSettings

    @Before
    fun setup() {
        interactorMock = mockk(relaxed = true)
        searchSettings = SearchSettings()

        viewModel = NewsListViewModel(
            interactorMock,
            InstrumentationRegistry.getInstrumentation().targetContext
        )
    }

    @Test
    fun getNewsDataStateReturnsSuccessWhenReceiveList() = runTest {

        coEvery { interactorMock.fetchNewsList(SearchSettings()) } returns Result.success(
            listOf()
        )
        viewModel.fetchNews(searchSettings)

        assertThat(viewModel.newsDataState.getOrAwaitValue(valueCountdown = 2)).isInstanceOf(DataState.Success::class.java)
    }

    @Test
    fun getNewsDataStateReturnsFailureWhenReceiveFailure() = runTest {
        coEvery { interactorMock.fetchNewsList(SearchSettings()) } returns Result.failure(
            IOException()
        )

        viewModel.fetchNews(searchSettings)

        assertThat(viewModel.newsDataState.getOrAwaitValue(valueCountdown = 2)).isInstanceOf(DataState.Failure::class.java)
    }

    @Test
    fun getNewsDataStateReturnsLoadingImmediatelyAfterFetchNewsCalled() = runTest {
        coEvery { interactorMock.fetchNewsList(SearchSettings()) } returns Result.success(listOf())

        viewModel.fetchNews(searchSettings)

        assertThat(viewModel.newsDataState.getOrAwaitValue()).isInstanceOf(DataState.Loading::class.java)
    }
}