package com.example.currencyapp.domain.model

sealed class DataState<out T : Any?> {
    object Loading : DataState<Nothing>()
    data class Success<out T : Any>(val result: T) : DataState<T>()
    data class Failure(val errorInfo: String = "Ooops") : DataState<Nothing>()
    object Default : DataState<Nothing>()
}