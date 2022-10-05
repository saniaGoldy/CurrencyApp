package com.example.currencyapp.ui.news.newsListFragment

import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.PublishDate
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class NewsFilterTest {

    private val testData = listOf<NewsData>(
        NewsData(
            "some news description1",
            PublishDate("2022-09-21", "2:02"),
            "Plumberg",
            listOf(),
            "breaking news!",
            "some url"
        ),
        NewsData(
            "some news description2",
            PublishDate("2022-09-21", "2:02"),
            "BBB",
            listOf(),
            "boring news!",
            "some url"
        )
    )

    @Test
    fun filterReturnsEmptyListWhenCashedDataStateIsFailure() {
        val newsFilter = NewsFilter(DataState.Failure())
        assertThat(newsFilter.filter(null)).isEmpty()
    }

    @Test
    fun filterReturnsEmptyListWhenCashedDataStateIsLoading() {
        val newsFilter = NewsFilter(DataState.Loading)
        assertThat(newsFilter.filter(null)).isEmpty()
    }

    @Test
    fun filterReturnsCashedListWhenKeywordIsEmpty() {
        val newsFilter = NewsFilter(DataState.Success(testData))
        assertThat(newsFilter.filter("")).isEqualTo(testData)
    }

    @Test
    fun filterReturnsCashedListWhenKeywordIsNull() {
        val newsFilter = NewsFilter(DataState.Success(testData))
        assertThat(newsFilter.filter(null)).isEqualTo(testData)
    }

    @Test
    fun filterReturnsFilteredListWhenKeywordIsMatching() {
        val newsFilter = NewsFilter(DataState.Success(testData))
        assertThat(newsFilter.filter("plumber")).isEqualTo(listOf(testData[0]))
    }

    @Test
    fun filterReturnsEmptyListWhenKeywordIsNotMatching() {
        val newsFilter = NewsFilter(DataState.Success(testData))
        assertThat(newsFilter.filter("5")).isEqualTo(listOf<NewsData>())
    }
}