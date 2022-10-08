package com.example.currencyapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(RateStoryTypeConverter::class)
data class CurrencyDataEntity(
    @PrimaryKey
    val iso4217Alpha: String,

    @ColumnInfo(name = "rate") val rate: Double,

    var rateStory: Map<String, Double>
)