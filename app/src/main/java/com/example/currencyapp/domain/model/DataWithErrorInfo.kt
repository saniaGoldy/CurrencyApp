package com.example.currencyapp.domain.model

sealed class DataWithErrorInfo<out T : Any?>{
    data class Success<out T : Any>(val value: T) : DataWithErrorInfo<T>()
    data class SuccessWithErrorInfo<out T : Any?>(val value: T, val errorInfo: String) :
        DataWithErrorInfo<T>()
    data class Failure(val errorInfo: String = "Ooops") : DataWithErrorInfo<Nothing>()
}