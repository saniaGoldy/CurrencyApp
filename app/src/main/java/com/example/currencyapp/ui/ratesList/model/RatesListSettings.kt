package com.example.currencyapp.ui.ratesList.model

import com.example.currencyapp.domain.model.Currencies

data class RatesListSettings(val currencyCode: String = Currencies.UAH.name, val precision: Int = 3)
