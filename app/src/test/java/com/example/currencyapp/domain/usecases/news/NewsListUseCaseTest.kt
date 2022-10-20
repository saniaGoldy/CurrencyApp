package com.example.currencyapp.domain.usecases.news

import com.example.currencyapp.domain.model.news.SearchSettings
import com.example.currencyapp.domain.repository.FakeNewsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class NewsListUseCaseTest {
    private lateinit var useCase: NewsListUseCase
    private lateinit var repository: FakeNewsRepository

    @Before
    fun setup() {
        repository = FakeNewsRepository()
        useCase = NewsListUseCase(repository, Dispatchers.IO)
    }

    @Test
    fun `fetchNewsList returns success when no exception`() = runBlocking {
        assertThat(useCase.fetchNewsList(SearchSettings())).isEqualTo(
            Result.success(
                repository.fetchNewsList(
                    SearchSettings()
                )
            )
        )
    }

    @Test
    fun `fetchNewsList returns failure when catches exception`() = runBlocking {
        repository.setFetchNewsListShouldThrowException(true)
        assertThat(useCase.fetchNewsList(SearchSettings()).isFailure).isTrue()
    }
}