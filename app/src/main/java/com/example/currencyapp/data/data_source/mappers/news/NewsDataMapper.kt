package com.example.currencyapp.data.data_source.mappers.news

import com.example.currencyapp.data.data_source.mappers.Mapper
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.domain.model.news.PublishDate
import javax.inject.Inject

class NewsDataMapper : Mapper<List<Data>, List<NewsData>> {
    override fun map(from: List<Data>): List<NewsData> {
        return from.map {
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