package com.example.currencyapp.data.remote.entities.news

data class SearchSettings(
    var keywords: String = "exchange rates",
    var tags: String = "money, UAH, Ukraine",
    var timeGap: String = NewsApiRequestOptions.date[2]
)
