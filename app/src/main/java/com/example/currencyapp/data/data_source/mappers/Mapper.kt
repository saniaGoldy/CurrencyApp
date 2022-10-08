package com.example.currencyapp.data.data_source.mappers

interface Mapper<From, To> {
    fun map(from: From): To
}