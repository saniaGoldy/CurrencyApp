package com.example.currencyapp.data.remote.entities.news

object NewsApiRequestOptions {
    val date = listOf(
        "today",
        "thisweek",
        "thismonth",
        "last7days",
        "last30days",
        "last3months",
        "yeartodate",
        "YYYY-MM-DD",
        "YYYY-MM-DD,YYYY-MM-DD"
    )
}