package com.example.currencyapp.data.remote.entities.news

enum class NewsApiRequestOptions(val queryParam: String) {
    Today("today"),
    ThisWeek("thisweek"),
    ThisMonth("thismonth"),
    Last7Days("last7days"),
    Last30Days("last30days"),
    Last3Months("last3months"),
    YearToDate("yeartodate"),
    DateFrom("YYYY-MM-DD"),
    DateFromTo("YYYY-MM-DD,YYYY-MM-DD")
}