package com.example.currencyapp.domain.model

data class UpdatableData<out T : Any> (val data: T, val isUpToDate: Boolean)