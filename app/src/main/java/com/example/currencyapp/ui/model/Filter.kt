package com.example.currencyapp.ui.model

interface Filter<out T> {
    fun filter(keyword: String?): List<T>
}