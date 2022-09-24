package com.example.currencyapp.domain.model

sealed class InconsistentData<out T: Any?> {
    data class Success<out T: Any>(val value: T): InconsistentData<T>()
    data class SuccessWithErrorInfo<out T: Any?>(val value: T, val errorInfo: String): InconsistentData<T>()
    data class Failure(val errorInfo: String = "Ooops"): InconsistentData<Nothing>()
}