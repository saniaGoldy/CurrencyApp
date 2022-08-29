package com.example.currencyapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class CurrencyFluctuation(
    @PrimaryKey
    val iso4217Alpha: String,
    @ColumnInfo(name = "rate") val rate: Double,
    @ColumnInfo(name = "rateDifference") val rateDifference: Double,
) {
    @Ignore
    val fullName: String = Currencies.valueOf(iso4217Alpha).fullName
}