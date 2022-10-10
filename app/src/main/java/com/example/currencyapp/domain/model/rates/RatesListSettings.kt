package com.example.currencyapp.domain.model.rates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatesListSettings(
    val currencyCode: String = Currencies.UAH.name,
    val precision: Int = 3,
    var isRatesIsUpToDateWithSettings: Boolean = true
) : Parcelable {
}
