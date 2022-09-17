package com.example.currencyapp.data.data_source.mappers

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.PublishDate

object NewsDataMapper {
    fun mapToNewsData(data: List<Data>): List<NewsData> {
        return data.map {
            val publishedAt =
                it.publishedAt.substringBefore(Data.DATE_TIME_DELIMITER) to
                        it.publishedAt.substringAfter(Data.DATE_TIME_DELIMITER)

            (NewsData(
                it.description,
                PublishDate(publishedAt.first, publishedAt.second),
                it.source,
                it.tags,
                it.title,
                it.url
            ))
        }
    }
}