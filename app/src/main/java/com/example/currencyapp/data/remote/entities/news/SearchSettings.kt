package com.example.currencyapp.data.remote.entities.news

import com.example.currencyapp.ui.news.NewsViewModel

data class SearchSettings(
    var keywords: String = "exchange rates",
    var tags: String = "money, UAH, Ukraine",
    var timeGap: String = NewsApiRequestOptions.ThisWeek.queryParam,
    var timeGapMode: NewsApiRequestOptions = NewsApiRequestOptions.ThisWeek
)
