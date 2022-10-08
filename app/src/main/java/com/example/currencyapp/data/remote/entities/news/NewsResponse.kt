package com.example.currencyapp.data.remote.entities.news


import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination
)