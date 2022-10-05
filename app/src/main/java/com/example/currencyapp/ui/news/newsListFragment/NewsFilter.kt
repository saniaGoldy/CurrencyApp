package com.example.currencyapp.ui.news.newsListFragment

import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.news.NewsData
import com.example.currencyapp.ui.model.Filter

class NewsFilter(private val cashedListState: DataState<List<NewsData>>?): Filter<NewsData> {
    override fun filter(keyword: String?): List<NewsData> {
        return if (cashedListState is DataState.Success) {
            if (!keyword.isNullOrEmpty())
                cashedListState.result.filter {
                    it.containsKeyword(keyword)
                }
            else
                cashedListState.result
        } else
            listOf()
    }
}