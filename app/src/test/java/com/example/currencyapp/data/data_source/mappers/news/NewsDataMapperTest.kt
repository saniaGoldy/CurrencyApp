package com.example.currencyapp.data.data_source.mappers.news

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.PublishDate
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

internal class NewsDataMapperTest{

    private lateinit var mapper: NewsDataMapper

    @Before
    fun setup(){
        mapper = NewsDataMapper()
    }

    @Test
    fun mapToNewsData() {
        val description = "description"
        val publishedAt = "2022:09:19T21:21"
        val publishDate = "2022:09:19"
        val publishTime = "21:21"
        val source = "source"
        val title = "title"
        val url = "URL"
        val dataFromAPI = listOf(
            Data(
                description,
                publishedAt,
                source,
                tags = listOf(),
                tickers = listOf(),
                title,
                url
            )
        )

        assertThat(mapper.map(dataFromAPI)).isEqualTo(
            listOf(
                NewsData(
                    description,
                    PublishDate(publishDate, publishTime),
                    source,
                    listOf(),
                    title,
                    url
                )
            )
        )
    }
}