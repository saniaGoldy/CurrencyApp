package com.example.currencyapp.data.remote.entities.news


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("description")
    val description: String,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("tickers")
    val tickers: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)